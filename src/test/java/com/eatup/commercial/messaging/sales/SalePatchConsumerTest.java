package com.eatup.commercial.messaging.sales;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eatup.commercial.service.sale.SaleService;
import com.eatup.commercial.util.json.MapperJsonObjeto;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

@ExtendWith(MockitoExtension.class)
class SalePatchConsumerTest {

    @Mock
    private SaleService saleService;

    @Mock
    private MapperJsonObjeto mapperJsonObjeto;

    private SalePatchConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new SalePatchConsumer(saleService, mapperJsonObjeto);
    }

    @Test
    void shouldApplyPatchWhenMessageIsValid() {
        String rawMessage = "{\"saleId\":\"5410bd50-76a4-49cb-b5d7-2fcae61d5473\",\"request\":{\"status\":\"COMPLETED\"}}";
        Message rabbitMessage = new Message(rawMessage.getBytes(StandardCharsets.UTF_8), new MessageProperties());
        SalePatchRequestedMessage dto = new SalePatchRequestedMessage(UUID.randomUUID(), new SalePatchRequest("COMPLETED"));

        when(mapperJsonObjeto.ejecutar(eq(rawMessage), eq(SalePatchRequestedMessage.class)))
                .thenReturn(Optional.of(dto));

        consumer.handleSalePatch(rabbitMessage);

        verify(saleService).applyPatch(dto);
    }

    @Test
    void shouldNotCallServiceWhenMessageCannotBeDeserialized() {
        String rawMessage = "{}";
        Message rabbitMessage = new Message(rawMessage.getBytes(StandardCharsets.UTF_8), new MessageProperties());

        when(mapperJsonObjeto.ejecutar(eq(rawMessage), eq(SalePatchRequestedMessage.class)))
                .thenReturn(Optional.empty());

        consumer.handleSalePatch(rabbitMessage);

        verify(saleService, never()).applyPatch(any());
    }

    @Test
    void shouldCatchServiceExceptionAndNotRethrow() {
        String rawMessage = "{\"saleId\":\"5410bd50-76a4-49cb-b5d7-2fcae61d5473\",\"request\":{\"status\":\"COMPLETED\"}}";
        Message rabbitMessage = new Message(rawMessage.getBytes(StandardCharsets.UTF_8), new MessageProperties());
        SalePatchRequestedMessage dto = new SalePatchRequestedMessage(UUID.randomUUID(), new SalePatchRequest("COMPLETED"));

        when(mapperJsonObjeto.ejecutar(eq(rawMessage), eq(SalePatchRequestedMessage.class)))
                .thenReturn(Optional.of(dto));
        doThrow(new RuntimeException("Business error")).when(saleService).applyPatch(dto);

        consumer.handleSalePatch(rabbitMessage);

        verify(saleService).applyPatch(dto);
    }
}

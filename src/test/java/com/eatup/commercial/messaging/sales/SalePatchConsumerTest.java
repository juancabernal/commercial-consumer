package com.eatup.commercial.messaging.sales;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eatup.commercial.service.sale.SaleService;
import com.eatup.commercial.util.json.MapperJsonObjeto;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SalePatchConsumerTest {

    @Mock
    private SaleService saleService;

    @Mock
    private MapperJsonObjeto mapperJsonObjeto;

    @InjectMocks
    private SalePatchConsumer consumer;

    @Test
    void shouldInvokeSaleServiceWhenMessageCanBeMapped() {
        SalePatchRequestedMessage dto = new SalePatchRequestedMessage(
                UUID.randomUUID(),
                new SalePatchRequest("COMPLETED")
        );
        when(mapperJsonObjeto.ejecutar("{}", SalePatchRequestedMessage.class)).thenReturn(Optional.of(dto));

        consumer.handleSalePatch("{}");

        verify(saleService).applyPatch(dto);
    }
}

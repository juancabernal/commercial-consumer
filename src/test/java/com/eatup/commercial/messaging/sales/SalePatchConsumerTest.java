package com.eatup.commercial.messaging.sales;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eatup.commercial.service.sale.SaleService;
import com.eatup.commercial.util.json.MapperJsonObjeto;
import java.util.Optional;
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
    private SalePatchConsumer salePatchConsumer;

    @Test
    void shouldNotCallServiceWhenMessageCannotBeDeserialized() {
        String rawMessage = "{invalid-json}";
        when(mapperJsonObjeto.ejecutar(rawMessage, SalePatchRequestedMessage.class)).thenReturn(Optional.empty());

        salePatchConsumer.handleSalePatch(rawMessage);

        verify(saleService, never()).applyPatch(any());
    }
}

package com.eatup.commercial.service.sale.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eatup.commercial.domain.sale.SaleDomain;
import com.eatup.commercial.domain.sale.SaleStatus;
import com.eatup.commercial.messaging.sales.SalePatchRequest;
import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import com.eatup.commercial.repository.sale.SaleRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    @Test
    void shouldUpdateSaleStatusWhenTransitionIsValid() {
        UUID saleId = UUID.randomUUID();
        SaleDomain sale = new SaleDomain();
        sale.setId(saleId);
        sale.setStatus(SaleStatus.IN_PROGRESS);
        sale.setModifiedDate(LocalDateTime.now().minusMinutes(5));

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(SaleDomain.class))).thenAnswer(invocation -> invocation.getArgument(0));

        saleService.applyPatch(new SalePatchRequestedMessage(saleId, new SalePatchRequest("COMPLETED")));

        assertEquals(SaleStatus.COMPLETED, sale.getStatus());
        verify(saleRepository).save(sale);
    }

    @Test
    void shouldFailWhenTransitionIsInvalid() {
        UUID saleId = UUID.randomUUID();
        SaleDomain sale = new SaleDomain();
        sale.setId(saleId);
        sale.setStatus(SaleStatus.COMPLETED);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        assertThrows(IllegalStateException.class,
                () -> saleService.applyPatch(new SalePatchRequestedMessage(saleId, new SalePatchRequest("IN_PROGRESS"))));
    }
}

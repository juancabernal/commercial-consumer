package com.eatup.commercial.service.sale.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eatup.commercial.domain.sale.SaleDomain;
import com.eatup.commercial.domain.sale.SaleStatus;
import com.eatup.commercial.exception.InvalidSaleStatusTransitionException;
import com.eatup.commercial.exception.SalePatchProcessingException;
import com.eatup.commercial.messaging.sales.SalePatchRequest;
import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import com.eatup.commercial.repository.sale.SaleRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    private SaleServiceImpl saleService;

    @BeforeEach
    void setUp() {
        saleService = new SaleServiceImpl(saleRepository);
    }

    @Test
    void shouldThrowWhenCompletedSaleIsPatched() {
        UUID saleId = UUID.randomUUID();
        SaleDomain sale = saleWithStatus(saleId, SaleStatus.COMPLETED);
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        InvalidSaleStatusTransitionException exception = assertThrows(InvalidSaleStatusTransitionException.class,
                () -> saleService.applyPatch(message(saleId, "IN_PROGRESS")));

        assertEquals("No se puede modificar una venta en estado COMPLETED.", exception.getMessage());
        verify(saleRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCancelledSaleIsPatched() {
        UUID saleId = UUID.randomUUID();
        SaleDomain sale = saleWithStatus(saleId, SaleStatus.CANCELLED);
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        InvalidSaleStatusTransitionException exception = assertThrows(InvalidSaleStatusTransitionException.class,
                () -> saleService.applyPatch(message(saleId, "IN_PROGRESS")));

        assertEquals("No se puede modificar una venta en estado CANCELLED.", exception.getMessage());
        verify(saleRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenStatusIsInvalid() {
        UUID saleId = UUID.randomUUID();
        SaleDomain sale = saleWithStatus(saleId, SaleStatus.CREATED);
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        SalePatchProcessingException exception = assertThrows(SalePatchProcessingException.class,
                () -> saleService.applyPatch(message(saleId, "FINISHED")));

        assertEquals("Estado de venta inválido recibido en el mensaje de patch: FINISHED.", exception.getMessage());
        verify(saleRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenMessageIsIncomplete() {
        SalePatchProcessingException exception = assertThrows(SalePatchProcessingException.class,
                () -> saleService.applyPatch(new SalePatchRequestedMessage()));

        assertEquals("El mensaje de patch de venta está incompleto. Debe incluir saleId y request.status.", exception.getMessage());
        verify(saleRepository, never()).findById(any());
    }

    @Test
    void shouldSaveWhenTransitionIsValid() {
        UUID saleId = UUID.randomUUID();
        SaleDomain sale = saleWithStatus(saleId, SaleStatus.CREATED);
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        saleService.applyPatch(message(saleId, "IN_PROGRESS"));

        assertEquals(SaleStatus.IN_PROGRESS, sale.getStatus());
        verify(saleRepository).save(sale);
    }

    private SaleDomain saleWithStatus(UUID id, SaleStatus status) {
        SaleDomain sale = new SaleDomain();
        sale.setId(id);
        sale.setStatus(status);
        return sale;
    }

    private SalePatchRequestedMessage message(UUID saleId, String nextStatus) {
        return new SalePatchRequestedMessage(saleId, new SalePatchRequest(nextStatus));
    }
}

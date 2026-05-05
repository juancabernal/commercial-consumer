package com.eatup.commercial.dto.table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TableSessionUpdateDTO {

    @NotNull(message = "El campo 'guestCount' es requerido")
    @Positive(message = "El campo 'guestCount' debe ser un número positivo mayor que cero")
    private Integer guestCount;

    public TableSessionUpdateDTO() {
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }
}

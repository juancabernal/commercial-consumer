package com.eatup.commercial.domain.purchase;

public enum PurchaseStatus {
    CREATED,
    APPROVED,
    CANCELLED,
    RECEIVED;

    public boolean canTransitionTo(PurchaseStatus next) {
        return switch (this) {
            case CREATED  -> next == APPROVED || next == CANCELLED;
            case APPROVED -> next == RECEIVED || next == CANCELLED;
            case CANCELLED, RECEIVED -> false;
        };
    }
}

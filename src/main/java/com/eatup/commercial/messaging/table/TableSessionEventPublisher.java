package com.eatup.commercial.messaging.table;

public interface TableSessionEventPublisher {

    void publishOpenSessionRequested(TableSessionOpenRequestedMessage message);

    void publishCloseSessionRequested(TableSessionCloseRequestedMessage message);
}

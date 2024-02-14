package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.bridge.impl.TicketBridge;

public class BridgeContext {

    private static final BridgeContext instance = new BridgeContext();

    private final TicketBridge ticketBridge;

    private BridgeContext() {
        this.ticketBridge = new TicketBridge();
    }

    public static BridgeContext getInstance() {
        return instance;
    }

    public static TicketBridge ticket() {
        return getInstance().ticketBridge;
    }

}

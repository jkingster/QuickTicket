package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.bridge.impl.CommentBridge;
import io.jacobking.quickticket.bridge.impl.TicketBridge;

public class BridgeContext {

    private static final BridgeContext instance = new BridgeContext();

    private final TicketBridge ticketBridge;
    private final CommentBridge commentBridge;

    private BridgeContext() {
        this.ticketBridge = new TicketBridge();
        this.commentBridge = new CommentBridge();
    }

    public static BridgeContext getInstance() {
        return instance;
    }

    public static TicketBridge ticket() {
        return getInstance().ticketBridge;
    }

    public static CommentBridge comment() {
        return getInstance().commentBridge;
    }

}

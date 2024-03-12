package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.bridge.impl.*;

public class BridgeContext {

    private static final BridgeContext instance = new BridgeContext();

    private final TicketBridge  ticketBridge;
    private final CommentBridge commentBridge;

    private final EmployeeBridge employeeBridge;
    private final EmailBridge    emailBridge;

    private final JournalBridge journalBridge;

    private BridgeContext() {
        this.ticketBridge = new TicketBridge();
        this.commentBridge = new CommentBridge();
        this.employeeBridge = new EmployeeBridge();
        this.emailBridge = new EmailBridge();
        this.journalBridge = new JournalBridge();
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

    public static EmployeeBridge employee() {
        return getInstance().employeeBridge;
    }

    public static EmailBridge email() {
        return getInstance().emailBridge;
    }

    public static JournalBridge journal() {
        return getInstance().journalBridge;
    }
}

package io.jacobking.quickticket.bridge;

import io.jacobking.quickticket.bridge.impl.*;

public class BridgeContext {

    private static BridgeContext instance = null;

    private final TicketBridge  ticketBridge;
    private final CommentBridge commentBridge;

    private final EmployeeBridge employeeBridge;
    private final EmailBridge    emailBridge;

    private final AlertSettingsBridge  alertSettingsBridge;
    private final ProfilePictureBridge profilePictureBridge;
    private final CompanyBridge        companyBridge;
    private final DepartmentBridge     departmentBridge;

    private BridgeContext() {
        this.ticketBridge = new TicketBridge();
        this.commentBridge = new CommentBridge();
        this.employeeBridge = new EmployeeBridge();
        this.emailBridge = new EmailBridge();
        this.alertSettingsBridge = new AlertSettingsBridge();
        this.profilePictureBridge = new ProfilePictureBridge();
        this.companyBridge = new CompanyBridge();
        this.departmentBridge = new DepartmentBridge();
    }

    public static BridgeContext getInstance() {
        if (instance == null) instance = new BridgeContext();
        return instance;
    }

    public static void rebuildInstance() {
        instance = null;
        getInstance();
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


    public static AlertSettingsBridge alertSettings() {
        return getInstance().alertSettingsBridge;
    }

    public static ProfilePictureBridge profilePicture() {
        return getInstance().profilePictureBridge;
    }

    public static CompanyBridge company() {
        return getInstance().companyBridge;
    }

    public static DepartmentBridge department() {
        return getInstance().departmentBridge;
    }
}

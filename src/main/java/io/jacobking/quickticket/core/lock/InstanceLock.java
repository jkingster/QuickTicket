package io.jacobking.quickticket.core.lock;


import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Notify;
import javafx.scene.control.ButtonType;

public class InstanceLock {

    private static final InstanceLock INSTANCE = new InstanceLock();

    private InstanceLock() {
        checkLock();
    }

    public static InstanceLock getInstance() {
        return INSTANCE;
    }


    public void deleteLock() {
        if (!FileIO.fileExists(FileIO.TARGET_LOCK)) {
            Notify.showError("Failed to find instance lock file.",
                    "Instances potentially cannot start....",
                    "Please submit bug report.");
            return;
        }

        if (!FileIO.deleteFile(FileIO.TARGET_LOCK)) {
            Notify.showError("Failed to delete instance lock file.",
                    "Instances potentially cannot start....",
                    "Please submit bug report.");
        }
    }


    private void checkLock() {
        if (FileIO.fileExists(FileIO.TARGET_LOCK)) {
            Notify.showConfirmation(
                    "Failed to launch quick ticket.",
                    "Another instance is already running!",
                    "Please close it out and try again. To force-delete the file, click the apply button." +
                            " Otherwise click no to ignore. Clicking apply can cause potential instability!",
                    ButtonType.APPLY, ButtonType.NO
            ).ifPresent(type -> {
                if (type == ButtonType.APPLY) {
                    attemptToDeleteAndStart();
                }
            });
            return;
        }

        if (!FileIO.createFile(FileIO.TARGET_LOCK)) {
            Notify.showError(
                    "Failed to create instance lock file.",
                    "Multiple instances can be started.",
                    "Please submit bug report.");
            return;
        }

        QuickTicket.launch();
    }

    private void attemptToDeleteAndStart() {
        deleteLock();
        if (FileIO.fileExists(FileIO.TARGET_LOCK)) {
            Notify.showError(
                    "Failed to force delete lock.",
                    "Failed to force delete lock.",
                    "Consider manually deleting the file in your AppData directory or submitting a bug report."
            );
            return;
        }
        QuickTicket.launch();
    }


}
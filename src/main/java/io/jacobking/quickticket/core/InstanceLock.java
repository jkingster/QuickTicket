package io.jacobking.quickticket.core;


import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Alerts;
import javafx.scene.control.ButtonType;

public class InstanceLock {

    private static InstanceLock instance;

    private boolean isUnlocked = false;

    private InstanceLock() {
    }

    public static synchronized InstanceLock getInstance() {
        if (instance == null) {
            instance = new InstanceLock();
        }
        return instance;
    }

    public void deleteLock() {
        if (!FileIO.fileExists(FileIO.TARGET_LOCK, true)) {
            Alerts.get().showError("Failed to find instance lock file.",
                    "Instances potentially cannot start....",
                    "Please submit bug report.");
            return;
        }

        if (!FileIO.deleteFile(FileIO.TARGET_LOCK)) {
            Alerts.get().showError("Failed to delete instance lock file.",
                    "Instances potentially cannot start....",
                    "Please submit bug report.");
        }
    }


    public void checkLock() {
        if (FileIO.fileExists(FileIO.TARGET_LOCK, true)) {
            Alerts.get().showWarningConfirmation(
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
            Alerts.get().showError(
                    "Failed to create instance lock file.",
                    "Multiple instances can be started.",
                    "Please submit bug report.");
            return;
        }
        this.isUnlocked = true;
    }

    private void attemptToDeleteAndStart() {
        deleteLock();
        if (FileIO.fileExists(FileIO.TARGET_LOCK, true)) {
            Logs.warn("Lock failed to delete on instance start.");
            Alerts.get().showError(
                    "Failed to force delete lock.",
                    "Failed to force delete lock.",
                    "Consider manually deleting the file in your AppData directory or submitting a bug report."
            );
            return;
        }
        this.isUnlocked = true;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }
}
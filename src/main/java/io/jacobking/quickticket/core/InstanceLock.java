package io.jacobking.quickticket.core;


import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Announcements;
import javafx.scene.control.ButtonBar;
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
            Announcements.get().showError("Failed to find instance lock file.",
                    "Instances potentially cannot start....",
                    "Please submit bug report.");
            return;
        }

        if (!FileIO.deleteFile(FileIO.TARGET_LOCK)) {
            Announcements.get().showError("Failed to delete instance lock file.",
                    "Instances potentially cannot start....",
                    "Please submit bug report.");
        }
    }


    public void checkLock() {
        if (FileIO.fileExists(FileIO.TARGET_LOCK, true)) {
            final ButtonType deleteLock = new ButtonType("Delete Lock", ButtonBar.ButtonData.YES);
            final ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.NO);

            Announcements.get().showWarningConfirmation(
                            "Warning",
                            "Instance Lock Found",
                            "Another instance is already running. To continue, delete the lock.", deleteLock, exit)
                    .ifPresent(type -> {
                        if (type == deleteLock) {
                            attemptToDeleteAndStart();
                        }
                    });
            return;
        }

        if (!FileIO.createFile(FileIO.TARGET_LOCK)) {
            Announcements.get().showError(
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
            Announcements.get().showError(
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
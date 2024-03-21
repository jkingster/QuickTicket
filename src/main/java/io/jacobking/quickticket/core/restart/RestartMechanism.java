package io.jacobking.quickticket.core.restart;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.Optional;

public class RestartMechanism {

    private final String    commandLine;

    public RestartMechanism() {
        final OSProcess osProcess = getOSProcess();
        this.commandLine = getCommandLine(Optional.ofNullable(osProcess));
    }

    private OSProcess getOSProcess() {
        final SystemInfo systemInfo = new SystemInfo();
        final OperatingSystem system = systemInfo.getOperatingSystem();
        final int currentPID = system.getProcessId();
        return system.getProcess(currentPID);
    }

    private String getCommandLine(final Optional<OSProcess> osProcess) {
        final OSProcess rawProcess = osProcess.orElseThrow(() ->
                new RestartException("OSProcess is null, invalid PID?"));

        final String commandLine = rawProcess.getCommandLine();
        if (commandLine == null || commandLine.isEmpty()) {
            throw new RestartException("CommandLine from OSProcess is null/empty!");
        }
        return commandLine;
    }

    public String getCommandLine() {
        return commandLine;
    }
}

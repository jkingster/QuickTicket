package io.jacobking.quickticket.core.restart;

import oshi.PlatformEnum;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// This idea is based off of Restart4J but more for my needs of controllability.
// https://github.com/Dansoftowner/Restart4j/blob/master/src/main/java/com/restart4j/CommandLineExecutor.java
public class PlatformControl {

    private static final String NUL_CHAR = "\00";

    private final Map<PlatformEnum, CommandExecutor> executorMap =
            new HashMap<>() {{
                put(PlatformEnum.LINUX, new SplitCommandLineExecutor());
                put(PlatformEnum.MACOS, new SplitCommandLineExecutor());
                put(PlatformEnum.WINDOWS, new BaseExecutor());
            }};

    public CommandExecutor getExecutorOrThrow(final PlatformEnum platform) {
        final CommandExecutor commandExecutor = executorMap.getOrDefault(platform, null);
        if (commandExecutor == null) {
            throw new RestartException("Could not fetch command executor, invalid OS?");
        }
        return commandExecutor;
    }

    private static class SplitCommandLineExecutor implements CommandExecutor {
        @Override public void execute(String commandLine) {
            final String[] commandLineArray = commandLine.split(NUL_CHAR);
            try {
                Runtime.getRuntime().exec(commandLineArray);
            } catch (IOException e) {
                throw new RestartException("Command Line: " + Arrays.toString(commandLineArray), e.fillInStackTrace());
            }
        }
    }

    private static class BaseExecutor implements CommandExecutor {
        @Override public void execute(String commandLine) {
            try {
                Runtime.getRuntime().exec(commandLine);
            } catch (IOException e) {
                throw new RestartException("Command Line: " + commandLine, e.fillInStackTrace());
            }
        }
    }

    public interface CommandExecutor {
        void execute(final String commandLine);
    }
}

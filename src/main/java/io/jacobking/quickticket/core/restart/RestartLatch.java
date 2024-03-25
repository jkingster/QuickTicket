package io.jacobking.quickticket.core.restart;

import javafx.application.Platform;
import javafx.concurrent.Task;
import oshi.SystemInfo;

import java.util.concurrent.CountDownLatch;

public class RestartLatch {

    private static final PlatformControl platformControl = new PlatformControl();

    private static final Runnable DEFAULT_PRE_POLICY = () -> {};

    private static final Runnable DEFAULT_TERMINATION_POLICY = Platform::exit;

    private final RestartMechanism                mechanism;
    private final PlatformControl.CommandExecutor executor;
    private final Runnable                        prePolicy;
    private final Runnable                        terminationPolicy;

    private RestartLatch(final Runnable prePolicy, final Runnable terminationPolicy) {
        this.mechanism = new RestartMechanism();
        this.executor = platformControl.getExecutorOrThrow(SystemInfo.getCurrentPlatform());
        this.prePolicy = prePolicy;
        this.terminationPolicy = terminationPolicy;
    }

    public static void restartApp(final Runnable prePolicy, final Runnable terminationPolicy) {
        new RestartLatch(prePolicy, terminationPolicy).restartApp();
    }

    private void restartApp() {
        final String commandLine = mechanism.getCommandLine();
        configureShutdownHook(commandLine);

        if (terminationPolicy == null) {
            DEFAULT_TERMINATION_POLICY.run();
        } else {
            terminationPolicy.run();
        }
    }

    private void configureShutdownHook(final String commandLine) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (prePolicy == null) {
                DEFAULT_PRE_POLICY.run();
            } else {

                final Task<Void> task = new Task<Void>() {
                    @Override protected Void call() throws Exception {
                        prePolicy.run();
                        return null;
                    }
                };

                task.run();
                countDownLatch.countDown();
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            executor.execute(commandLine);
        }));


    }


}

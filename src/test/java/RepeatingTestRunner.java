import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * Created by moiseev on 12.10.17.
 */
public class RepeatingTestRunner extends BlockJUnit4ClassRunner {
    private final Class<?> type;

    private boolean stop = false;

    public RepeatingTestRunner(final Class<?> type) throws InitializationError {
        super(type);
        this.type = type;
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {

        if (shouldStopOnFailure(method)) {
            notifier.addListener(new FailTriggerRunListener());
        }

        for (int i = 0; i < repeatCount(method); i++) {
            if (stop) {
                return;
            }
            super.runChild(method, notifier);
        }
    }

    private int repeatCount(final FrameworkMethod method) {
        if (intermittent(type)) {
            return repetition(type);
        }
        if (intermittent(method)) {
            return repetition(method);
        }
        return 1;
    }

    private boolean shouldStopOnFailure(final FrameworkMethod method) {
        if (intermittent(type)) {
            return stopOnFailure(type);
        }
        if (intermittent(method)) {
            return stopOnFailure(method);
        }
        return false;
    }

    private static boolean intermittent(final FrameworkMethod method) {
        return method.getAnnotation(Repeating.class) != null;
    }

    private static boolean intermittent(final Class<?> type) {
        return type.getAnnotation(Repeating.class) != null;
    }

    private static int repetition(final FrameworkMethod method) {
        return method.getAnnotation(Repeating.class).count();
    }

    private static int repetition(final Class<?> type) {
        return type.getAnnotation(Repeating.class).count();
    }

    private static boolean stopOnFailure(final FrameworkMethod method) {
        return method.getAnnotation(Repeating.class).stopOnFailure();
    }

    private static boolean stopOnFailure(final Class<?> type) {
        return type.getAnnotation(Repeating.class).stopOnFailure();
    }

    private class FailTriggerRunListener extends RunListener {

        @Override
        public void testFailure(final Failure failure) throws Exception {
            super.testFailure(failure);
            stop = true;
        }

    }

}
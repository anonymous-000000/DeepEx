package pkg;
public class Test {
public void uncaughtException(Thread t, Throwable e) {
if (e instanceof TestException) {
passed[TEST_EXCEPTION_IN_UNCAUGHT] = true;
throw new UncaughtException();
} else
super.uncaughtException(t, e);
}
}
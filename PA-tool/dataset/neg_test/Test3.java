package pkg;
public class Test {
public void run() {
setPriority(javaPriority);
int nativePriority = TestNatives.getSchedulingPriority(this);
System.out.println("Thread[" + javaPriority + "]: " + nativePriority);
if (nativePriority != expectedNativePriority) {
fail("nativePriority != expectedNativePriority, \n\tnativePriority="
+ nativePriority + " expectedNativePriority=" + expectedNativePriority
+ "\n\tjavaPriority=" + javaPriority);
}
}
}
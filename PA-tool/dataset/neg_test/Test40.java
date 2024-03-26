package pkg;
public class Test {
public static int compare(byte[] b1, byte[] b2) {
return WritableComparator.compareBytes(b1, 0, b1.length,
b2, 0, b2.length);
}
}
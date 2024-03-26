package pkg;
public class Test {
public static byte[] toBytes(TextMessage message) {
return MessageBodyToBytesConverter.toBytes(message, null);
}
}
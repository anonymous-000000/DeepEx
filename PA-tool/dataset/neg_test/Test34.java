package pkg;
public class Test {
private void readObject(ObjectInputStream s)
throws IOException, ClassNotFoundException {
ObjectInputStream.GetField fields = s.readFields();
byte[] tmp = (byte[])fields.get("buf", null);
if (tmp == null)
throw new InvalidObjectException("buf is null and should not be!");
buf = tmp.clone();
len = fields.get("len", 0L);
if (buf.length != len)
throw new InvalidObjectException("buf is not the expected size");
origLen = fields.get("origLen", 0L);
blob = (Blob) fields.get("blob", null);
}
}
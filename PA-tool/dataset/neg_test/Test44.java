package pkg;
public class Test {
@Override
public void close() {
if (!closed) {
closed = true;
request.close();
request.endTransIfRequired();
}
}
}
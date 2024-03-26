package pkg;
public class Test {
public void loadServers(InputStream in) {
int count = -1;
if (updateUtil()) {
count = persistentUtil.loadComponents(in);
}
if (count < 0) {
loadServers(in, servers);
}
}
}
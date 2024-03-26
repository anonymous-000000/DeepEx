package pkg;
public class Test {
public void setServerGroupIdss(List<String> serverGroupIdss) {
this.serverGroupIdss = serverGroupIdss;
if (serverGroupIdss != null) {
for (int i = 0; i < serverGroupIdss.size(); i++) {
putBodyParameter("ServerGroupIds." + (i + 1) , serverGroupIdss.get(i));
}
}
}
}
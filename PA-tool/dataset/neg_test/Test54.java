package pkg;
public class Test {
protected static void aftertest() throws Exception {
if (null != restTestHarness) {
restTestHarness.close();
restTestHarness = null;
}
if (null != jetty) {
jetty.stop();
jetty = null;
}
if (null != tmpSolrHome) {
PathUtils.deleteDirectory(tmpSolrHome);
tmpSolrHome = null;
}
System.clearProperty("managed.schema.mutable");
unchooseDefaultFeatureFormat();
}
}
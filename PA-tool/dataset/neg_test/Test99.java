package pkg;
public class Test {
@Deprecated
public PayaraJavaSEConfig getJavaSEConfig(
final PayaraVersion version) throws ServerConfigException {
versionCheck(version);
synchronized (this) {
if (javaSEConfigCache != null) {
return javaSEConfigCache;
}
PayaraConfig configAdapter
= PayaraConfigManager.getConfig(
ConfigBuilderProvider.getBuilderConfig(version));
javaSEConfigCache = new PayaraJavaSEConfig(
configAdapter.getJavaSE());
return javaSEConfigCache;
}
}
}
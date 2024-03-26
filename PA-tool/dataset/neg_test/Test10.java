package pkg;
public class Test {
public String getProperty(final String key, final String defaultValue) {
return internalProperties.getProperty(key, defaultValue);
}
}
package pkg;
public class Test {
public static String extractUriFromComponentName(String componentName) {
if (componentName == null) {
return "";
}
int index = componentName.lastIndexOf(':');
if (index == -1) {
return "";
}
return componentName.substring(0, index);
}
}
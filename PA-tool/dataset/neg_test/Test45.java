package pkg;
public class Test {
public String getStyle(Map<String, Object> context) {
return FlexibleStringExpander.expandString(linkMenuItem.getLinkStyle(), context).trim();
}
}
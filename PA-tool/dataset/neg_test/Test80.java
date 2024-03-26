package pkg;
public class Test {
private static StackFrameType forClassName(String fullyQualifiedClassName) {
if (fullyQualifiedClassName.endsWith("Test")
&& !fullyQualifiedClassName.equals(
"androidx.test.internal.runner.junit3.NonLeakyTestSuite$NonLeakyTest")) {
return StackFrameType.NEVER_REMOVE;
}
for (StackFrameType stackFrameType : StackFrameType.values()) {
if (stackFrameType.belongsToType(fullyQualifiedClassName)) {
return stackFrameType;
}
}
return StackFrameType.NEVER_REMOVE;
}
}
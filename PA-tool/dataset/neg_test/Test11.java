package pkg;
public class Test {
int testStringWithConstantExpression(String str) {
return switch (str) {
case "A" -> 1;
case null -> -1;
case String s -> s.length();
};
}
}
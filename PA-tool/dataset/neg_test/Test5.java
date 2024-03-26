package pkg;
public class Test {
public void test4285() {
check(
"Integrate[((A + B*x)*(b*x + c*x^2)^2)/x^(9/2), x]",
"(-2*A*b^2)/(3*x^(3/2)) - (2*b*(b*B + 2*A*c))/Sqrt[x] + 2*c*(2*b*B + A*c)*Sqrt[x] + (2*B*c^2*x^(3/2))/3",
779);
}
}
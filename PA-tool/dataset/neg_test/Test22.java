package pkg;
public class Test {
private String expectedOutput(final VTLResourceTestCase testCase) {
String expectedOutput;
if(testCase.getExpectedException()>0) {
expectedOutput = Integer.toString(testCase.getExpectedException());
} else if(UtilMethods.isSet(testCase.getExpectedJSON())) {
expectedOutput = testCase.getExpectedJSON();
} else {
expectedOutput = testCase.getExpectedOutput();
}
return expectedOutput;
}
}
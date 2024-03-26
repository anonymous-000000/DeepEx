package pkg;
public class Test {
public static String getMethodText(String className, String methodName, Object[] args)
{
StringBuffer methodSignature = new StringBuffer();
for (int i = 0; args != null && i < args.length; i++)
{
methodSignature.append(ObjectUtils.getClassName(args[i]));
methodSignature.append(i == (args.length - 1) ? "" : ", ");
}
return className + "." + methodName + "(" + methodSignature + ") ";
}
}
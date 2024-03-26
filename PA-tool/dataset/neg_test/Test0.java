package pkg;
public class Test {
private static boolean isHex(char p_char)
{
return (isDigit(p_char) || (p_char >= 'a' && p_char <= 'f')
|| (p_char >= 'A' && p_char <= 'F'));
}
}

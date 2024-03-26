package pkg;
public class Test {
private static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
return cs.toString().indexOf(searchChar.toString(), start);
}
}
package pkg;
public class Test {
@Override
public void enterRule(ParseTreeListener listener) {
if ( listener instanceof ANTLRv3ParserListener ) ((ANTLRv3ParserListener)listener).enterBlock(this);
}
}
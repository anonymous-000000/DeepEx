package pkg;
public class Test {
static void convertToSAXParseException(XMLParseException e) throws SAXException {
Exception ex = e.getException();
if (ex == null) {
LocatorImpl locatorImpl = new LocatorImpl();
locatorImpl.setPublicId(e.getPublicId());
locatorImpl.setSystemId(e.getExpandedSystemId());
locatorImpl.setLineNumber(e.getLineNumber());
locatorImpl.setColumnNumber(e.getColumnNumber());
throw new SAXParseException(e.getMessage(), locatorImpl);
}
if (ex instanceof SAXException) {
throw (SAXException) ex;
}
throw new SAXException(ex);
}
}
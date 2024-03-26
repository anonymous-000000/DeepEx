package pkg;
public class Test {
private static void assertExchangeTotalBufferedBytes(LocalExchange exchange, int pageCount)
{
assertEquals(exchange.getBufferedBytes(), retainedSizeOfPages(pageCount));
}
}
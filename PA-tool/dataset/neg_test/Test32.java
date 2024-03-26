package pkg;
public class Test {
public com.anychart.charts.Waterfall hatchFillPalette(String settings) {
APIlib.getInstance().addJSLine(String.format(Locale.US, jsBase + ".hatchFillPalette(%s);", wrapQuotes(settings)));
return this;
}
}
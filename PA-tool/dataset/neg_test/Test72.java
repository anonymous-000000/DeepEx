package pkg;
public class Test {
@Override
public RoundedMoney[] divideAndRemainder(Number divisor) {
BigDecimal bd = MoneyUtils.getBigDecimal(divisor);
if (isOne(bd)) {
return new RoundedMoney[]{this, new RoundedMoney(0L, getCurrency(), rounding)};
}
BigDecimal[] dec = number.divideAndRemainder(MoneyUtils.getBigDecimal(divisor), Optional.ofNullable(
monetaryContext.get(MathContext.class)).orElse(MathContext.DECIMAL64));
return new RoundedMoney[]{new RoundedMoney(dec[0], currency, rounding),
new RoundedMoney(dec[1], currency, rounding).with(rounding)};
}
}
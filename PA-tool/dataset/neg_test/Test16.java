package pkg;
public class Test {
public static LocalDateTime of(int year, Month month, int dayOfMonth, int hour, int minute, int second) {
LocalDate date = LocalDate.of(year, month, dayOfMonth);
LocalTime time = LocalTime.of(hour, minute, second);
return new LocalDateTime(date, time);
}
}
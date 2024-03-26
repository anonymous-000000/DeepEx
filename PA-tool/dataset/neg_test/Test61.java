package pkg;
public class Test {
public Builder forAllStringValues(Consumer<String> action) {
this.forAllStringValuesConsumer = action;
return this;
}
}
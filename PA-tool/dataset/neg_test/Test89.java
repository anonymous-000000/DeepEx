package pkg;
public class Test {
@Test
public void testToIterableWithFailure() {
Multi<Integer> multi = Multi.createFrom().<Integer> emitter(e -> e.emit(1).emit(0).complete())
.map(v -> 4 / v);
assertThatThrownBy(() -> multi.subscribe().asIterable().forEach(i -> {
})).isInstanceOf(ArithmeticException.class).hasMessageContaining("by zero");
}
}
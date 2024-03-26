package pkg;
public class Test {
@Test
public void writesValuesToGraphite() throws Exception {
graphite.connect();
graphite.send("name", "value", 100);
graphite.close();
assertThat(unpickleOutput())
.isEqualTo("name value 100\n");
}
}
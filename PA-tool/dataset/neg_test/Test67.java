package pkg;
public class Test {
public static Set<Field> getFields(Class<?> clazz) {
final Set<Field> fields = new LinkedHashSet<Field>();
final Class<?> parentClazz = clazz.getSuperclass();
Collections.addAll(fields, clazz.getDeclaredFields());
if (null != parentClazz) {
fields.addAll(getFields(parentClazz));
}
return fields;
}
}
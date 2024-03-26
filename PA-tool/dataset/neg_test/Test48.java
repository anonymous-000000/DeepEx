package pkg;
public class Test {
@Override
public int compareTo(Role role) {
if (!role.getServer().equals(getServer())) {
throw new IllegalArgumentException("Only roles from the same server can be compared for order");
}
return ROLE_COMPARATOR.compare(this, role);
}
}
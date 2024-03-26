package pkg;
public class Test {
public ListUserPermissionsResponse listUserPermissions(String userName) {
Preconditions.checkState(StringUtils.isNotBlank(userName), "userName is required");
return execGet(String.format(Suffix.LIST_USER_PERM.get(), userName), readers.get(Suffix.LIST_USER_PERM));
}
}
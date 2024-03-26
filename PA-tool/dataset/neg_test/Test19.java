package pkg;
public class Test {
protected static void setAutoCommit(Connection conn, boolean newValue) throws SQLException {
SqlTransaction.setAutoCommit(conn, newValue);
}
}
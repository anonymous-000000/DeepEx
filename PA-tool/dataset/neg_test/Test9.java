package pkg;
public class Test {
public HDBConnection(HDBClient client) {
if (client == null) {
throw new NullPointerException();
}
this.client = client;
this.leaderChangedErrors = client
.getStatsLogger()
.getCounter("leaderChangedErrors");
this.maxConnectionsPerServer =
client.getConfiguration().getInt(ClientConfiguration.PROPERTY_MAX_CONNECTIONS_PER_SERVER, ClientConfiguration.PROPERTY_MAX_CONNECTIONS_PER_SERVER_DEFAULT);
this.routes = new ConcurrentHashMap<>();
}
}
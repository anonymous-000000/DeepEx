package pkg;
public class Test {
CryptoMaterialReloader(Path tlsOptionsConfigFile,
ScheduledExecutorService scheduler,
MutableX509TrustManager trustManager,
MutableX509KeyManager keyManager) {
this.tlsOptionsConfigFile = tlsOptionsConfigFile;
this.scheduler = scheduler;
this.trustManager = trustManager;
this.keyManager = keyManager;
}
}
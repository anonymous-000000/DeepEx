package pkg;
public class Test {
@Test
public void test_prefer_server_cipher_suites_can_be_changed() throws Exception {
final File file1 = testKeyStoreGenerator.generateKeyStore("teststore", "JKS", "passwd1", "passwd2");
final String keystorePath1 = file1.getAbsolutePath();
final Tls tls1 = new Tls.Builder()
.withKeystorePath(keystorePath1)
.withKeystoreType("JKS")
.withKeystorePassword("passwd1")
.withPrivateKeyPassword("passwd2")
.withProtocols(new ArrayList<>())
.withTruststorePath(keystorePath1)
.withTruststoreType("JKS")
.withTruststorePassword("passwd1")
.withClientAuthMode(Tls.ClientAuthMode.OPTIONAL)
.withCipherSuites(new ArrayList<>())
.withHandshakeTimeout(10000)
.build();
final SslContext sslContext1 = sslFactory.getSslContext(tls1);
final SslHandler sslHandler1 = sslFactory.getSslHandler(socketChannel, tls1, sslContext1);
final SSLParameters sslParameters1 = sslHandler1.engine().getSSLParameters();
final boolean engineDefaultPreferServerCipherSuites = sslParameters1.getUseCipherSuitesOrder();
final File file2 = testKeyStoreGenerator.generateKeyStore("teststore", "JKS", "passwd1", "passwd2");
final String keystorePath2 = file2.getAbsolutePath();
final Tls tls2 = new Tls.Builder()
.withKeystorePath(keystorePath2)
.withKeystoreType("JKS")
.withKeystorePassword("passwd1")
.withPrivateKeyPassword("passwd2")
.withProtocols(new ArrayList<>())
.withTruststorePath(keystorePath2)
.withTruststoreType("JKS")
.withTruststorePassword("passwd1")
.withClientAuthMode(Tls.ClientAuthMode.OPTIONAL)
.withCipherSuites(new ArrayList<>())
.withPreferServerCipherSuites(!engineDefaultPreferServerCipherSuites)
.withHandshakeTimeout(10000)
.build();
final SslContext sslContext2 = sslFactory.getSslContext(tls2);
final SslHandler sslHandler2 = sslFactory.getSslHandler(socketChannel, tls2, sslContext2);
final SSLParameters sslParameters2 = sslHandler2.engine().getSSLParameters();
assertNotEquals(tls1, tls2);
assertNotEquals(sslContext1, sslContext2);
assertNotEquals(sslParameters1, sslParameters2);
assertArrayEquals(sslParameters1.getCipherSuites(), sslParameters2.getCipherSuites());
assertArrayEquals(sslParameters1.getApplicationProtocols(), sslParameters2.getApplicationProtocols());
assertArrayEquals(sslParameters1.getProtocols(), sslParameters2.getProtocols());
assertEquals(sslParameters1.getServerNames(), sslParameters2.getServerNames());
assertEquals(sslParameters1.getNeedClientAuth(), sslParameters2.getNeedClientAuth());
assertEquals(sslParameters1.getWantClientAuth(), sslParameters2.getWantClientAuth());
assertEquals(sslParameters1.getEnableRetransmissions(), sslParameters2.getEnableRetransmissions());
assertEquals(sslParameters1.getMaximumPacketSize(), sslParameters2.getMaximumPacketSize());
assertEquals(sslParameters1.getEndpointIdentificationAlgorithm(), sslParameters2.getEndpointIdentificationAlgorithm());
assertEquals(sslParameters1.getSNIMatchers(), sslParameters2.getSNIMatchers());
assertEquals(sslParameters1.getAlgorithmConstraints(), sslParameters2.getAlgorithmConstraints());
assertEquals(engineDefaultPreferServerCipherSuites, sslParameters1.getUseCipherSuitesOrder());
assertEquals(!engineDefaultPreferServerCipherSuites, sslParameters2.getUseCipherSuitesOrder());
}
}
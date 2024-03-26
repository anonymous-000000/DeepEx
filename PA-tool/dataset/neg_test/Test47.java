package pkg;
public class Test {
@Column(name="ipAddr")
@XmlElement(name="ipAddress")
@Type(type="org.opennms.netmgt.model.InetAddressUserType")
@XmlJavaTypeAdapter(InetAddressXmlAdapter.class)
public InetAddress getIpAddr() {
return this.m_ipAddr;
}
}
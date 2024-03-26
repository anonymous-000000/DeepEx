package pkg;
public class Test {
@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (!(o instanceof BACnetUnconfirmedServiceRequestUnconfirmedCOVNotificationMultiple)) {
return false;
}
BACnetUnconfirmedServiceRequestUnconfirmedCOVNotificationMultiple that =
(BACnetUnconfirmedServiceRequestUnconfirmedCOVNotificationMultiple) o;
return (getSubscriberProcessIdentifier() == that.getSubscriberProcessIdentifier())
&& (getInitiatingDeviceIdentifier() == that.getInitiatingDeviceIdentifier())
&& (getTimeRemaining() == that.getTimeRemaining())
&& (getTimestamp() == that.getTimestamp())
&& (getListOfCovNotifications() == that.getListOfCovNotifications())
&& super.equals(that)
&& true;
}
}
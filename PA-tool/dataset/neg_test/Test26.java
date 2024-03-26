package pkg;
public class Test {
@Override
public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
cancelPolling();
if (bridgeStatusInfo.getStatus() == ThingStatus.ONLINE) {
internalInitialize();
} else {
updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
}
}
}
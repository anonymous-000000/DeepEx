package pkg;
public class Test {
private Integer addListenerWithSubject(ObjectName name,
MarshalledObject<NotificationFilter> filter,
Subject delegationSubject,
boolean reconnect)
throws InstanceNotFoundException, IOException {
final boolean debug = logger.debugOn();
if (debug)
logger.debug("addListenerWithSubject",
"(ObjectName,MarshalledObject,Subject)");
final ObjectName[] names = new ObjectName[] {name};
final MarshalledObject<NotificationFilter>[] filters =
Util.cast(new MarshalledObject<?>[] {filter});
final Subject[] delegationSubjects = new Subject[] {
delegationSubject
};
final Integer[] listenerIDs =
addListenersWithSubjects(names,filters,delegationSubjects,
reconnect);
if (debug) logger.debug("addListenerWithSubject","listenerID="
+ listenerIDs[0]);
return listenerIDs[0];
}
}
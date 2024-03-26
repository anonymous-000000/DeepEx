package pkg;
public class Test {
private static MethodHandle INDY_call682 () throws Throwable {
if (INDY_call682 != null) return INDY_call682;
CallSite cs = (CallSite) MH_bootstrap682 ().invokeWithArguments(MethodHandles.lookup(), "gimmeTarget", MT_bootstrap682 ());
return cs.dynamicInvoker();
}
}
package pkg;
public class Test {
@ReactMethod
public void logout(Promise promise){
final Activity activity = getCurrentActivity();
if(activity != null) {
manager.logout(activity, promise);
}else{
promise.reject(new Throwable());
}
}
}
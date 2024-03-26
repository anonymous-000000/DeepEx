package pkg;
public class Test {
@NonNull
public static String getAppVersionName() {
return getAppVersionName(Utils.getApp().getPackageName());
}
}
package pkg;
public class Test {
public static String getCommandLine(ProcessBuilder pb) {
if (pb == null) {
return "null";
}
StringBuilder cmd = new StringBuilder();
for (String s : pb.command()) {
cmd.append(s).append(" ");
}
return cmd.toString().trim();
}
}
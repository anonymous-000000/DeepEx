package evaluation;

import org.eclipse.jdt.core.dom.*;

public class APICallSpec {

    public String methodName;
    public int arity;
    public int startLineNum;
    public int endLineNum;

    public APICallSpec(Expression method, int startLineNum, int endLineNum) {
        if (method instanceof SuperMethodInvocation methodSFA) {
            this.methodName = methodSFA.getName().getIdentifier();
            this.arity = methodSFA.arguments().size();
        } else if (method instanceof MethodInvocation methodSFA) {
            this.methodName = methodSFA.getName().getIdentifier();
            this.arity = methodSFA.arguments().size();
        } else {
            throw new RuntimeException("The input expression is not a method invocation");
        }

        this.startLineNum = startLineNum;
        this.endLineNum = endLineNum;
    }

}

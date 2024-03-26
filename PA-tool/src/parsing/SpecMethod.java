package parsing;

import utils.JavaASTUtil;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.Set;

public class SpecMethod {

    public String id;
    public String methodName;
    public String[] parameterNames;
    public String[] parameterTypes;
    public int arity;
    public String[] exceptions;
    
    public SpecMethod(String methodUniqueID, MethodDeclaration method, Set<String> exceptions) {
        // represent the method name, and parameter names of the method
        this.id = methodUniqueID;
        this.methodName = method.getName().getIdentifier();

        this.arity = method.parameters().size();
        this.parameterNames = getParameterNames(method);
        this.parameterTypes = getParameterTypes(method);

        this.exceptions = new String[exceptions.size()];
        this.exceptions = exceptions.toArray(this.exceptions);
    }

    private String[] getParameterNames(MethodDeclaration method) {
        String[] parameterNames = new String[method.parameters().size()];
        for (int i = 0; i < method.parameters().size(); i++) {
            SingleVariableDeclaration d = (SingleVariableDeclaration) (method.parameters().get(i));
            parameterNames[i] = d.getName().getIdentifier();
        }

        return parameterNames;
    }

    private String[] getParameterTypes(MethodDeclaration method) {
        String[] parameterTypes = new String[method.parameters().size()];

        for (int i = 0; i < method.parameters().size(); i++) {
            SingleVariableDeclaration d = (SingleVariableDeclaration) (method.parameters().get(i));
            String type = JavaASTUtil.getSimpleType(d.getType());
            parameterTypes[i] = type;
        }

        return parameterTypes;
    }
}

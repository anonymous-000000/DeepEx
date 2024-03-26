package evaluation.json;

public class ArgToCalls {
    String callName;
    String resolvedMethod;
    String[] paramTypes;
    String returnType;
    int position;
    int lineNumber;
    int columnNumber;

    public String getCallName () {
        return callName;
    }

    public String getResolvedMethod () {
        return resolvedMethod;
    }

    public String[] getParamTypes () {
        return paramTypes;
    }

    public String getReturnType () {
        return returnType;
    }

    public int getPosition () {
        return position;
    }

    public int getLineNumber () {
        return lineNumber;
    }

    public int getColumnNumber () {
        return columnNumber;
    }
}

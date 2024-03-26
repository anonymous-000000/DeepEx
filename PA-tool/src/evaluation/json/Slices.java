package evaluation.json;

public class Slices {
    TargetObj targetObj;
    DefinedBy definedBy;
    InvokedCalls[] invokedCalls;
    ArgToCalls[] argToCalls;

    public TargetObj getTargetObj() {
        return targetObj;
    }

    public DefinedBy getDefinedBy () {
        return definedBy;
    }

    public InvokedCalls[] getInvokedCalls () {
        return invokedCalls;
    }

    public ArgToCalls[] getArgToCalls () {
        return argToCalls;
    }
}

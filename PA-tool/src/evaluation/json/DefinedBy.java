package evaluation.json;

public class DefinedBy {
    String name;
    String typeFullName;
    int lineNumber;
    int columnNumber;
    String label;

    public String getName () {
        return name;
    }

    public String getTypeFullName () {
        return typeFullName;
    }

    public int getLineNumber () {
        return lineNumber;
    }

    public int getColumnNumber () {
        return columnNumber;
    }

    public String getLabel () {
        return label;
    }
}

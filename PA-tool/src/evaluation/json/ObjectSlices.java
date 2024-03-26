package evaluation.json;

public class ObjectSlices {
    String code;
    String fullName;
    String fileName;
    Slices[] slices;
    int lineNumber;
    int columnNumber;

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFileName () {
        return fileName;
    }

    public Slices[] getSliceArr () {
        return slices;
    }

    public int getLineNumber () {
        return lineNumber;
    }

    public int getColumnNumber () {
        return columnNumber;
    }

    public String toString() {
        return code + fullName + ", " + fileName;
    }
}

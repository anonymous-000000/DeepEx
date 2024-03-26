package evaluation.json;

public class UserDefinedTypes {
    String name;
    Fields[] fields;
    Procedures[] procedures;
    String fileName;
    int lineNumber;
    int columnNumber;

    public String getName () {
        return name;
    }

    public Fields[] getFields () {
        return fields;
    }

    public Procedures[] getProcedures () {
        return procedures;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}

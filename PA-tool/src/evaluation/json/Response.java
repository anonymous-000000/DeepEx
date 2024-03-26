package evaluation.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class Response {
    ObjectSlices[] objectSlices;
    UserDefinedTypes[] userDefinedTypes;

    public ObjectSlices[] getObjectSlices() {
        return objectSlices;
    }

    public UserDefinedTypes[] getUserDefinedTypes() {
        return userDefinedTypes;
    }
}

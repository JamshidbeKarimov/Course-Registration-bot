package jsonFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;

public class CollectionsTypeFactory {
    public static JavaType listOf(Class clazz) {
        return TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
    }

    public static <T> TypeReference<List> erasedListOf(Class<T> ignored) {
        return new TypeReference<List>(){};
    }
}

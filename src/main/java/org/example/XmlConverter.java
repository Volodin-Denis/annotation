package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class XmlConverter {
    public String convertToXml(Object object) throws Exception {
        checkIfSerializable(object);
        initializeObject(object);
        return getXmlString(object);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void checkIfSerializable(Object object) {
        if (Objects.isNull(object)) {
            throw new RuntimeException("The object is null");
        }

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(XmlSerializable.class)) {
            throw new RuntimeException("The class " + clazz.getSimpleName() + " is not annotated with XmlSerializable");
        }
    }

    private void initializeObject(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Init.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }

    private String getXmlString(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        Map<String, String> xmlElementsMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XmlElement.class)) {
                xmlElementsMap.put(getKey(field), (String) field.get(object));
            }
        }

        String xmlString = xmlElementsMap.entrySet()
            .stream()
            .map(entry -> "\t\t<" + entry.getKey() + ">"
                + entry.getValue() + "</" + entry.getKey() + ">")
            .collect(Collectors.joining("\n"));
        return
            "<xml>\n"
            + "\t<" + getName(clazz) + ">\n"
            + xmlString
            + "\n\t</" + getName(clazz) + ">"
            + "\n</xml>";
    }

    private String getKey(Field field) {
        String value = field.getAnnotation(XmlElement.class).key();
        return value.isEmpty() ? field.getName() : value;
    }

    private String getName(Class<?> clazz) {
        String value = clazz.getAnnotation(XmlSerializable.class).name();
        return value.isEmpty() ? clazz.getSimpleName() : value;
    }
}

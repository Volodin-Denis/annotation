package org.example;

public class Main {
    public static void main(String[] args) throws Exception{
        User user = new User("userName");
        XmlConverter converter = new XmlConverter();
        System.out.println(converter.convertToXml(user));
    }
}
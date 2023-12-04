package org.example;

@XmlSerializable(name = "thisUser")
public class User {
    @XmlElement
    private String login;
    @XmlElement(key = "fullName")
    private String name;
    private String password;

    public User(String name) {
        this.name = name;
    }

    @Init
    private void initDefaultValues() {
        this.login = "U_" + name;
        this.password = login.toLowerCase() + "-12345";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

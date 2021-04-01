package com.example.rest.beans;

public class User {
    protected String login;
    protected String password;
    final protected Role role;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }
    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }
}
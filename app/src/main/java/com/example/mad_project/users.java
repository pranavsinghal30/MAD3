package com.example.mad_project;

public class users {
    private String username, password, type;

    public users() { }

    public users(String u, String p, String t) {
        this.username= u;
        this.password = p;
        this.type = t;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }
}

package com.amazonaws.youruserpools.Data;

/**
 * Created by Leandro on 3/10/2017.
 */

public class User {
    private String origin;
    private String email;
    private String environment;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}

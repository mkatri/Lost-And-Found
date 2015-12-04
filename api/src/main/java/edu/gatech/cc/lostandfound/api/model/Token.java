package edu.gatech.cc.lostandfound.api.model;

/**
 * Created by mkatri on 12/3/15.
 */
public class Token {

    String token;

    public Token(String token) {
        this.token = token;
    }

    public String get() {
        return this.token;
    }

    public void set(String token) {
        this.token = token;
    }
}

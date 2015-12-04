package edu.gatech.cc.lostandfound.api.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by mkatri on 12/3/15.
 */
@Entity
public class Token {

    @Id
    Long id;
    String token;

    public Token() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

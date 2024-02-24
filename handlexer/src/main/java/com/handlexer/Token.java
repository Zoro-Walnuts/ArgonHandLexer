package com.handlexer;

public class Token {
    // private String name;
    private TokenTypes type;
    private String value;

    public Token(TokenTypes type, String value) {
        // this.name = name;
        this.type = type;
        this.value = value;
    }

    // getters and setters
    /*
     * deprecate name field
     * public String getName() {
     * return name;
     * }
     * 
     * public void setName(String name) {
     * this.name = name;
     * }
     */

    public TokenTypes getType() {
        return type;
    }

    public void setType(TokenTypes type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

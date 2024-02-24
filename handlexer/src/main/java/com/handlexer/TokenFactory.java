package com.handlexer;

public class TokenFactory {
    public static Token createToken(TokenTypes type, String value) {
        return new Token(type, value);
    }
}

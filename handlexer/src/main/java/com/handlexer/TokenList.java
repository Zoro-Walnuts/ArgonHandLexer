package com.handlexer;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<Token> tokens;

    public TokenList() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public void addToken(TokenTypes type, String value) {
        this.tokens.add(TokenFactory.createToken(type, value));
    }

    public int findTokenExist(Token token) {
        int i = 0;
        for (Token t : this.tokens) {
            // if value and type is the same
            if (t.getValue().equals(token.getValue()) && t.getType().equals(token.getType())) {
                // System.out.println("MATCH FOUND");
                return i;
            }
            i++;
        }
        return -1;
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public int getSize() {
        return this.tokens.size();
    }

    public String printLatestToken() {
        return this.tokens.get(getSize() - 1).getType().name();
    }

    public Token getToken(int x) {
        return tokens.get(x);
    }

    public void printTokens() {
        int i = 1;
        for (Token token : this.tokens) {
            System.out.printf("token %-4d | type: %-10s | value: %-30s%n",
                    i, token.getType().toString(), token.getValue().toString());
            i++;
        }
    }
}

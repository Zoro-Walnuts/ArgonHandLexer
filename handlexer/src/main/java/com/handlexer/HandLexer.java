package com.handlexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

enum TokenTypes {
    // single character tokens (0-7)
    ASSIGN, COMMA, SEMICOLON, OPENPAR,
    CLOSEPAR, OPENBR, CLOSEBR, INVERT,

    // possibly two character tokens (8-23)
    ADD, INC, ADDASSIGN,
    SUB, DEC, SUBASSIGN, ARROW,
    MUL, MULASSIGN,
    DIV, DIVASSIGN, LINECMT,
    EXP, EXPASSIGN,
    GT, GTE,
    LT, LTE,

    // logic operators (24-27)
    IS, NOT, AND, OR,

    // escape sequences (28-35)
    NEWLINE, HORZTAB,
    CARGRET, BACKSPC,
    BACKSLSH, SINGQUOT,
    DOUBQUOT, FORMFEED,

    // literals (36-39)
    IDENT, NUMLIT, STRLIT, BOOLLIT,

    // reserved words (40-56)
    CATALYZE, DECOMPOSE, DISTILL, FUNNEL, FILTER,
    FERMENT, FROM, INERT, INPUT, PRINT, PRINTLN,
    PRINTERR, REACTIVE, TITRATE, TO, WHEN, STEP
}

enum States {
    START,
    SYM_CROSS,
    SYM_DASH,
    SYM_ASTER,
    SYM_SLASH,
    SYM_CARET,
    SYM_GT,
    SYM_LT,
    ESCSEQ,
    NUMLITERAL, BIN, OCT, HEX,
    IDENT,
    RELLOGIC, // relational and logical
    INVALID
}

public class HandLexer {
    int numErrs = 0;
    String errorsStr = "";

    // err handling
    private String error(int line, String message) {
        String errString = "[line " + line + "] Error: " + message;
        System.err.println(errString);
        errorsStr += errString + "\n";
        numErrs++;
        return errString;
    }

    // the scanning part
    private String codeString = "";
    private String output = "";
    private String subst = "";
    private int line = 1;
    private int curr;
    private States currState = States.START;

    private char advance() { // advance to the next character
        return codeString.charAt(curr++);
    }

    private char backtrack() {
        return codeString.charAt(--curr);
    }

    private boolean endOfCode() {
        return curr >= codeString.length();
    }

    public void Analyze(File in) throws IOException {

        // store source code
        FileInputStream fis = new FileInputStream(in);
        Scanner sc = new Scanner(fis);

        while (sc.hasNextLine()) {
            codeString += sc.nextLine() + "\n";
        }
        sc.close();
        // System.out.println(codeString);

        // setting up the tokenslist
        TokenList tokenList = new TokenList();

        // getting the tokens :3
        char c;
        while (!endOfCode()) {
            c = advance();
            // System.out.println("at position " + curr + " on line " + line + " is the
            // character " + c);
            switch (currState) {
                case START:
                    switch (c) {
                        // single character cases
                        case ',':
                            // create new token; add to list
                            tokenList.addToken(TokenTypes.COMMA, String.valueOf(c), line);
                            // token name -> output
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case ';':
                            tokenList.addToken(TokenTypes.SEMICOLON, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '(':
                            tokenList.addToken(TokenTypes.OPENPAR, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case ')':
                            tokenList.addToken(TokenTypes.CLOSEPAR, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '{':
                            tokenList.addToken(TokenTypes.OPENBR, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '}':
                            tokenList.addToken(TokenTypes.CLOSEBR, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '!':
                            tokenList.addToken(TokenTypes.INVERT, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;

                        case '=':
                            tokenList.addToken(TokenTypes.ASSIGN, String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            break;

                        // newlines
                        case '\n':
                            line++;
                            break;

                        // double character cases
                        case '+':
                            currState = States.SYM_CROSS;
                            break;

                        case '-':
                            currState = States.SYM_DASH;
                            break;

                        case '*':
                            currState = States.SYM_ASTER;
                            break;

                        case '/':
                            currState = States.SYM_SLASH;
                            break;

                        case '^':
                            currState = States.SYM_CARET;
                            break;

                        case '>':
                            currState = States.SYM_GT;
                            break;

                        case '<':
                            currState = States.SYM_LT;
                            break;

                        // logical/relational operators (excluding invert "!")
                        case '.':
                            currState = States.RELLOGIC;
                            subst = "";
                            subst += String.valueOf(c);
                            break;

                    } // end char switch START
                    break;

                // double character cases
                case SYM_CROSS:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.ADDASSIGN, "+" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.ADD, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_CROSS
                    break;

                case SYM_DASH:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.SUBASSIGN, "-" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        case '>':
                            tokenList.addToken(TokenTypes.ARROW, "-" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.SUB, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_DASH
                    break;

                case SYM_ASTER:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.MULASSIGN, "*" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.MUL, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_ASTER
                    break;

                case SYM_SLASH:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.DIVASSIGN, "/" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.DIV, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_SLASH
                    break;

                case SYM_CARET:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.EXPASSIGN, "^" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.EXP, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_CARET
                    break;

                case SYM_GT:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.GTE, ">" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.GT, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_GT
                    break;

                case SYM_LT:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenTypes.LTE, "<" + String.valueOf(c), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                        default:
                            c = backtrack();
                            tokenList.addToken(TokenTypes.LT, String.valueOf(codeString.charAt(curr - 1)), line);
                            output += tokenList.printLatestToken() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_LT
                    break;

                case RELLOGIC:
                    subst += String.valueOf(c);

                    if (c == '.') { // end of rellogics operator found
                        switch (subst) {
                            case ".is.":
                                tokenList.addToken(TokenTypes.IS, subst, line);
                                output += tokenList.printLatestToken() + "\n";
                                break;
                            case ".not.":
                                tokenList.addToken(TokenTypes.NOT, subst, line);
                                output += tokenList.printLatestToken() + "\n";
                                break;
                            case ".and.":
                                tokenList.addToken(TokenTypes.AND, subst, line);
                                output += tokenList.printLatestToken() + "\n";
                                break;
                            case ".or.":
                                tokenList.addToken(TokenTypes.OR, subst, line);
                                output += tokenList.printLatestToken() + "\n";
                                break;

                            default:
                                error(line, subst + " is not a valid relational/logical operator!");
                                break;
                        }
                        subst = "";
                        currState = States.START;
                    } // end if . found

                    if (c == '\n') { // newline
                        error(line, "relational/logical operator closure not found!");
                        line++;
                        subst = "";
                        currState = States.START;
                    }

            } // end case state switch
        } // end scanner while

        tokenList.printTokens();
        System.out.println();
        output += "\n" + numErrs + " ERRORS FOUND!\n" + errorsStr;
        System.out.println(output);
    }
}

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
    RELLOG, // relational and logical
    INVALID
}

public class HandLexer {
    static int numErrs = 0;

    // err handling
    static String error(int line, String message) {
        String errString = "[line " + line + "] Error: " + message;
        System.err.println(errString);
        numErrs++;
        return errString;
    }

    // the scanning part
    private String codeString = "";
    private String output = "";
    private String subst = "";
    private int line = 1;
    private int start, curr;
    private States currState = States.START;

    private char advance() { // advance to the next character
        return codeString.charAt(curr++);
    }

    private char backtrack() {
        return codeString.charAt(curr--);
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
            switch (currState) {
                case START:
                    switch (c) {
                        // singe character cases
                        case ',':
                            // create new token; add to list
                            tokenList.addToken(TokenTypes.COMMA, String.valueOf(c));
                            // token name -> output
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case ';':
                            tokenList.addToken(TokenTypes.SEMICOLON, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '(':
                            tokenList.addToken(TokenTypes.OPENPAR, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case ')':
                            tokenList.addToken(TokenTypes.CLOSEPAR, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '{':
                            tokenList.addToken(TokenTypes.OPENBR, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '}':
                            tokenList.addToken(TokenTypes.CLOSEBR, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;
                        case '!':
                            tokenList.addToken(TokenTypes.INVERT, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;

                        case '=':
                            tokenList.addToken(TokenTypes.ASSIGN, String.valueOf(c));
                            output += tokenList.printLatestToken() + "\n";
                            break;

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

                        // logical/relational ops
                        case '.':
                            currState = States.RELLOG;
                            start = curr;
                            break;

                        // numerical literal
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            currState = States.NUMLITERAL;
                            start = curr - 1;
                            break;

                        // identifiers (behold! the entire alphabet)

                    } // end char switch START
                    break;

                // double character cases
                case SYM_CROSS:
                    switch (c) {
                        case '+':
                            tokenList.addToken(TokenFactory.createToken(
                                    "INC", TokenTypes.INC, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "ADDASSIGN", TokenTypes.ADDASSIGN, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "ADD", TokenTypes.ADD, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_CROSS
                    break;

                case SYM_DASH:
                    switch (c) {
                        case '-':
                            tokenList.addToken(TokenFactory.createToken(
                                    "DEC", TokenTypes.DEC, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "SUBASSIGN", TokenTypes.SUBASSIGN, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '>':
                            tokenList.addToken(TokenFactory.createToken(
                                    "ARROW", TokenTypes.ARROW, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "SUB", TokenTypes.SUB, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_DASH
                    break;

                case SYM_ASTER:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "MULASSIGN", TokenTypes.MULASSIGN, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "MUL", TokenTypes.MUL, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_ASTER
                    break;

                case SYM_SLASH:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "DIVASSIGN", TokenTypes.DIVASSIGN, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "DIV", TokenTypes.DIV, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_SLASH
                    break;

                case SYM_CARET:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "EXPASSIGN", TokenTypes.EXPASSIGN, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "EXP", TokenTypes.EXP, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_CARET
                    break;

                case SYM_GT:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "GTE", TokenTypes.GTE, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "GT", TokenTypes.GT, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_GT
                    break;

                case SYM_LT:
                    switch (c) {
                        case '=':
                            tokenList.addToken(TokenFactory.createToken(
                                    "LTE", TokenTypes.LTE, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                        case '\n':
                            line++;
                            // TODO: Add case for numerical literal and identifiers
                        default:
                            tokenList.addToken(TokenFactory.createToken(
                                    "LT", TokenTypes.LT, null, line));
                            output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            currState = States.START;
                            break;
                    } // end char switch SYM_LT
                    break;

                case RELLOG:
                    if (c == '.') { // end of operator found; find match
                        // TODO: Make string append instead of substring
                        subst = codeString.substring(start, curr - 1);
                        switch (subst) {
                            case "is":
                                tokenList.addToken(TokenFactory.createToken(
                                        "IS", TokenTypes.IS, null, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                                break;
                            case "not":
                                tokenList.addToken(TokenFactory.createToken(
                                        "NOT", TokenTypes.NOT, null, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                                break;
                            case "and":
                                tokenList.addToken(TokenFactory.createToken(
                                        "AND", TokenTypes.AND, null, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                                break;
                            case "or":
                                tokenList.addToken(TokenFactory.createToken(
                                        "OR", TokenTypes.OR, null, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                                break;
                            default:
                                error(line, subst + " is not a logical/relational operator!");
                                output += "[line " + line + "] Error: " + subst
                                        + " is not a logical/relational operator!\n";
                                break;
                        }
                        currState = States.START;
                        // TODO: Make eof error (unclosed operator)
                    }
                    break; // end case state RELLOG

                case NUMLITERAL:
                    // TODO: Make string append instead of substring
                    subst = codeString.substring(start, curr - 1);
                    switch (c) {
                        // TODO: Move non-integer cases to after seeing 0 from start state and others
                        case 'b':
                            currState = States.BIN;
                            break;

                        case 'c':
                            currState = States.OCT;
                            break;

                        case 'x':
                            currState = States.HEX;
                            break;

                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break;

                        // TODO: add symbols cases

                        // TODO: make this into seeing a logical/relational operator
                        case '.':
                            // purge current lexeme
                            while (c != ' ' && c != '\n' && c != '\t') {
                                c = advance();
                                subst = codeString.substring(start, curr - 1);
                            }
                            error(line, subst + " is a float/decimal! (not supported)");
                            output += "[line " + line + "] Error: " + subst
                                    + " is a float/decimal! (not supported)\n";
                            if (c == '\n')
                                line++;
                            currState = States.START;
                            break;

                        case '\n':
                        case ' ':
                        case '\t':
                            int i;
                            if ((i = tokenList.findTokenExist( // check if token already in list
                                    TokenFactory.createToken("NUMLIT_" + subst, TokenTypes.NUMLIT, subst,
                                            line))) >= 0) {
                                output += tokenList.getToken(i).getName() + "\n";
                            } else {
                                tokenList.addToken(TokenFactory.createToken(
                                        "NUMLIT_" + subst, TokenTypes.NUMLIT, subst, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            }
                            currState = States.START;
                            if (c == '\n')
                                line++;
                            break;

                        default:
                            // purge current lexeme
                            while (c != ' ' && c != '\n' && c != '\t') {
                                c = advance();
                                subst = codeString.substring(start, curr - 1);
                            }
                            error(line, subst + " is not a valid number!");
                            output += "[line " + line + "] Error: " + subst
                                    + " is not a valid number!\n";
                            if (c == '\n')
                                line++;
                            currState = States.START;
                            break;
                    } // end char switch NUMLIT
                    break;

                case BIN:
                    subst = codeString.substring(start, curr - 1);
                    switch (c) {
                        case '0':
                        case '1':
                            break;

                        case '\n':
                        case ' ':
                        case '\t':
                            int i;
                            if ((i = tokenList.findTokenExist( // check if token already in list
                                    TokenFactory.createToken("NUMLIT_" + subst, TokenTypes.NUMLIT, subst,
                                            line))) >= 0) {
                                output += tokenList.getToken(i).getName() + "\n";
                            } else {
                                tokenList.addToken(TokenFactory.createToken(
                                        "NUMLIT_" + subst, TokenTypes.NUMLIT, subst, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            }
                            currState = States.START;
                            if (c == '\n')
                                line++;
                            break;

                        default:
                            // purge current lexeme
                            while (c != ' ' && c != '\n' && c != '\t') {
                                c = advance();
                                subst = codeString.substring(start, curr - 1);
                            }
                            error(line, subst + " is not a valid BINARY number!");
                            output += "[line " + line + "] Error: " + subst
                                    + " is not a valid BINARY number!\n";
                            if (c == '\n')
                                line++;
                            currState = States.START;
                            break;
                    } // end char switch BIN
                    break;

                case OCT:
                    subst = codeString.substring(start, curr - 1);
                    switch (c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                            break;

                        case '\n':
                        case ' ':
                        case '\t':
                            int i;
                            if ((i = tokenList.findTokenExist( // check if token already in list
                                    TokenFactory.createToken("NUMLIT_" + subst, TokenTypes.NUMLIT, subst,
                                            line))) >= 0) {
                                output += tokenList.getToken(i).getName() + "\n";
                            } else {
                                tokenList.addToken(TokenFactory.createToken(
                                        "NUMLIT_" + subst, TokenTypes.NUMLIT, subst, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            }
                            currState = States.START;
                            if (c == '\n')
                                line++;
                            break;

                        default:
                            // purge current lexeme
                            while (c != ' ' && c != '\n' && c != '\t') {
                                c = advance();
                                subst = codeString.substring(start, curr - 1);
                            }
                            error(line, subst + " is not a valid OCTAL number!");
                            output += "[line " + line + "] Error: " + subst
                                    + " is not a valid OCTAL number!\n";
                            if (c == '\n')
                                line++;
                            currState = States.START;
                            break;
                    } // end char switch OCT
                    break;

                case HEX:
                    subst = codeString.substring(start, curr - 1);
                    switch (c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            break;

                        case '\n':
                        case ' ':
                        case '\t':
                            int i;
                            if ((i = tokenList.findTokenExist( // check if token already in list
                                    TokenFactory.createToken("NUMLIT_" + subst, TokenTypes.NUMLIT, subst,
                                            line))) >= 0) {
                                output += tokenList.getToken(i).getName() + "\n";
                            } else {
                                tokenList.addToken(TokenFactory.createToken(
                                        "NUMLIT_" + subst, TokenTypes.NUMLIT, subst, line));
                                output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
                            }
                            currState = States.START;
                            if (c == '\n')
                                line++;
                            break;

                        default:
                            // purge current lexeme
                            while (c != ' ' && c != '\n' && c != '\t') {
                                c = advance();
                                subst = codeString.substring(start, curr - 1);
                            }
                            error(line, subst + " is not a valid HEXADECIMAL number!");
                            output += "[line " + line + "] Error: " + subst
                                    + " is not a valid HEXADECIMAL number!\n";
                            if (c == '\n')
                                line++;
                            currState = States.START;
                            break;
                    } // end char switch HEX
                    break;

            } // end case state switch
        } // end scanner while

        tokenList.printTokens();
        System.out.println(output);

        // if (c == '=') {
        // tokenList.addToken(TokenFactory.createToken(
        // "ASSIGN", TokenTypes.ASSIGN, Character.toString(c), line));
        // output += tokenList.getToken(tokenList.getSize() - 1).getName() + "\n";
    }
}

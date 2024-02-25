package com.handlexer;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        HandLexer lex = new HandLexer();
        if (args.length > 0) {
            try {
                File file = new File("handlexer/src/main/resources/" + args[0]);
                // System.out.println(file.getAbsolutePath());
                lex.Analyze(new File(file.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                System.out.println("File not found :3");
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } catch (IllegalStateException ise) {
                System.out.println(ise.getMessage());
            }
        } else {
            System.out.println("Invalid File Input!");
        }
    }
}

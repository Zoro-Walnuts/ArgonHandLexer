package com.handlexer;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HandLexer lex = new HandLexer();
        if (args.length > 0) {
            try {
                // set file for command argument:
                File file = new File("handlexer/src/main/resources/" + args[0]);

                // set file for user input prompt:
                // Scanner scanner = new Scanner(System.in);
                // String input_file;
                // System.out.println("Input file name/path: ");
                // input_file = scanner.nextLine();
                // File file = new File(input_file);

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

package com.handlexer;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HandLexer lex = new HandLexer();
        try {
            File file;
            if (args.length > 0) {
                // check argument for file:
                file = new File(args[0]);
            } else {
                // get file from user input prompt:
                Scanner scanner = new Scanner(System.in);
                String input_file;
                System.out.print("Input file name/path: ");
                input_file = scanner.nextLine();
                file = new File(input_file);
                scanner.close();
            }

            lex.Analyze(new File(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found :3");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (IllegalStateException ise) {
            System.out.println(ise.getMessage());
        }
    }
}

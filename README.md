# ArgonHandLexer

Hand lexer made in java for the Argon Proglang

---

### Running and executing

1. Download the jar file (under [releases](https://github.com/Zoro-Walnuts/ArgonHandLexer/releases/tag/v6))
2. Open cmd in the directory where the jar file is located
3. Put your txt file with Argon code in the same directory as the jar file
4. type the command `java -jar ArgonHandLexer.jar yourtextfile.txt`. If no txt argument is given, you will get prompted for the filename.


#### Using the Batch File

1. Download the jar file (under [releases](https://github.com/Zoro-Walnuts/ArgonHandLexer/releases/tag/v6))
2. Download the batch file (under [releases](https://github.com/Zoro-Walnuts/ArgonHandLexer/releases/tag/v6))
3. Put the above two files and your txt file in the same directory
4. Drag the txt file into the batch file

> NOTE: This won't show the console output, but will still generate output files

---

## About

The lexer generates 3 things:

1. A Token List - A chronological list of all the token objects successfully generated
2. A Token Set - An unordered list of all identifiers and literals successfully generated
3. An Output File - The final list of tokens in plaintext format for a parser to read
4. A Neat Output File - The final list of tokens but formatted with spaces and newlines

The TokenList and the Token Set can only be seen in the command line, the output and neat output will also be printed in the command line.
At the end of the output and neat output file will be a total count of errors, and all the errors including type and what line each is in.

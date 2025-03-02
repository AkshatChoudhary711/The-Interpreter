import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TokenType {
    LEFT_PAREN, RIGHT_PAREN
}


class MyScanner{
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    MyScanner(String source) {
        this.source = source;
}


class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;

    Token(TokenType type, String lexeme, Object literal) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        if (args.length < 2) {
            System.err.println("Usage: ./your_program.sh tokenize <filename>");
            System.exit(1);
        }

        String command = args[0];
        String filename = args[1];

        if (!command.equals("tokenize")) {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }

        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(filename));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        // Uncomment this block to pass the first stage
        //
        for(char c : fileContents.toCharArray()) {
        if(c=='(') System.out.println("LEFT_PAREN ( null");
        else if(c==')') System.out.println("RIGHT_PAREN ( null");
        }


        System.out.println("EOF  null"); // Placeholder, remove this line when implementing the scanner
    }

    }
}

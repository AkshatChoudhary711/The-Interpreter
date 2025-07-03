import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
//        System.err.println("Logs from your program will appear here!");

        if (args.length < 2) {
            System.err.println("Usage: ./your_program.sh tokenize <filename>");
            System.exit(1);
        }

        String command = args[0];
        String filename = args[1];

        if (!command.equals("tokenize") && !command.equals("parse") && !command.equals("evaluate")) {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }else if(command.equals("tokenize")){
            String fileContents = "";
            try {
                fileContents = Files.readString(Path.of(filename));
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                System.exit(1);
            }

            MyScanner myScanner = new MyScanner(fileContents);
            myScanner.scanSource();
            for (Token token : myScanner.getTokens()) {
                System.out.println(token.toString());

            }
            if(myScanner.getErrCode() !=0) System.exit(myScanner.getErrCode());
        }
        else if(command.equals("evaluate")){
            String fileContents = "";
            try {
                fileContents = Files.readString(Path.of(filename));
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                System.exit(1);
            }

            MyScanner myScanner = new MyScanner(fileContents);
            myScanner.scanSource();
            Parser parser = new Parser(myScanner.getTokens());
            Expr expr = parser.parse();
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(expr);
            if (Lox.hadRuntimeError) {
                System.exit(70);
            }
            if (Lox.hadError) {
                System.exit(65);
            }


        }
        else{
            String fileContents = "";
            try {
                fileContents = Files.readString(Path.of(filename));
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                System.exit(1);
            }

            MyScanner myScanner = new MyScanner(fileContents);
            myScanner.scanSource();
            Parser parser = new Parser(myScanner.getTokens());
            Expr expr = parser.parse();
            AstPrinter printer = new AstPrinter();
            System.out.println(printer.print(expr));



        }

    }

}


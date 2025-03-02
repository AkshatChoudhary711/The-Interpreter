import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

enum TokenType {
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    STAR,
    PLUS,
    MINUS,
    SEMICOLON,
    COMMA,
    SLASH,
    DOT,
    EOF,
    EQUAL,
    BANG,
    EQUAL_EQUAL,
    BANG_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,
    STRING

}

public class MyScanner {
    HashMap<Character, TokenType> singleTokenMap = new HashMap<>();
    HashMap<String, TokenType> multiTokenMap = new HashMap<>();

    {
        singleTokenMap.put('(', TokenType.LEFT_PAREN);
        singleTokenMap.put(')', TokenType.RIGHT_PAREN);
        singleTokenMap.put('{', TokenType.LEFT_BRACE);
        singleTokenMap.put('}', TokenType.RIGHT_BRACE);
        singleTokenMap.put('*', TokenType.STAR);
        singleTokenMap.put('+', TokenType.PLUS);
        singleTokenMap.put('-', TokenType.MINUS);
        singleTokenMap.put(';', TokenType.SEMICOLON);
        singleTokenMap.put(',', TokenType.COMMA);
        singleTokenMap.put('/', TokenType.SLASH);
        singleTokenMap.put('.', TokenType.DOT);
        singleTokenMap.put('=', TokenType.EQUAL);
        singleTokenMap.put('!', TokenType.BANG);
        singleTokenMap.put('>', TokenType.GREATER);
        singleTokenMap.put('<', TokenType.LESS);
    } //single tokens

    {
        multiTokenMap.put("==", TokenType.EQUAL_EQUAL);
        multiTokenMap.put("!=", TokenType.BANG_EQUAL);
        multiTokenMap.put(">=", TokenType.GREATER_EQUAL);
        multiTokenMap.put("<=", TokenType.LESS_EQUAL);
    }//double tokens


    private final String source;
    private int curLine;
    private int current;
    private final List<Token> tokens = new ArrayList<>();
    private int errCode = 0;

    public int getErrCode() {
        return errCode;
    }

    public List<Token> getTokens() {
        return tokens;
    }


    MyScanner(String source) {
        this.source = source;
        this.current = 0;
        this.curLine = 1;

    }

    void addToken(TokenType tp, String ch, Object val,int line) {
        tokens.add(new Token(tp, ch, val,line));
    }

    void scan() {
        char c = advance();
        switch (c) {
            case '(', ')', '{', '}', '.', '*', '-', '+', ',', ';':
                addToken(singleTokenMap.get(c), Character.toString(c), null,this.curLine);
                break;

            //Checking single slash and comments
            case '/':
                if (peek() == '/') {
                    //A comment goes until the end of the line
                    while (peek() != '\n' && peek() != '\0') advance();

                } else {
                    addToken(singleTokenMap.get(c), Character.toString(c), null,this.curLine);
                }
                break;

            //checking  for relational operators
            case '=', '<', '>', '!':
                if (peek() == '=') {
                    String op = Character.toString(c) + advance();
                    addToken(multiTokenMap.get(op), op, null,curLine);
                } else {
                    addToken(singleTokenMap.get(c), Character.toString(c), null,curLine);
                }
                break;

            case '\r', '\t', ' ':
                break;

            case '\n':    //Increment line number
                curLine++;
                break;

            case '"':
                StringBuilder str = new StringBuilder();
                while(peek()!='"' && peek()!='\0'){
                    if(peek()=='\n') {
                    curLine++;
//                    advance();
//                    continue;
                    }

                    str.append(advance());
                }
                if(peek()=='\0'){
                    System.err.println("[line " + curLine + "] Error: Unterminated string.");
                    errCode = 65;
                }else{
                    advance();
                    addToken(TokenType.STRING, '"'+str.toString()+'"', str.toString(),curLine);
                }
                break;

            //If character not in enum
            default:
                System.err.println("[line " + curLine + "] Error: Unexpected character: " + (int)c);
                errCode = 65;

        }
    }

    void scanAll() {
        while (current < source.length()) {
            scan();
        }
        addToken(TokenType.EOF, "", null,curLine);
    }

    char peek() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    char advance() {
        if (current >= source.length()) return '\0';
        char cur = source.charAt(current);
        current++;
        return cur;
    }
}

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
    EOF
}

public class MyScanner {
    HashMap<Character, TokenType> singleTokenMap = new HashMap<>();
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
    } //single tokens

    private final String source;
    private int current;
    private final List<Token> tokens = new ArrayList<>();

    public List<Token> getTokens() {
        return tokens;
    }


    MyScanner(String source) {
        this.source = source;
        this.current = 0;
    }

    void addToken(TokenType tp, String ch, Object val) {
        tokens.add(new Token(tp, ch, val));
    }

    void scan() {
        char c = advance();
        switch (c) {
            case '(', ')', '{', '}', '.', '*', '-', '+', ',', ';', '/':
                addToken(singleTokenMap.get(c), Character.toString(c), null);
                break;
            default:
                System.err.println("[line "+1+"] Error: Unexpected character: "+ c);
                System.exit(65);
        }
    }

    void scanAll() {
        while (current < source.length()) {
            scan();
        }
        addToken(TokenType.EOF,  "", null);
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

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
    STRING,
    NUMBER,
    IDENTIFIER,
    AND,
    OR,
    IF,
    ELSE,
    WHILE,
    FOR,
    FUN,
    RETURN,
    VAR,
    CLASS,
    THIS,
    SUPER,
    NIL,
    TRUE,
    FALSE,
    PRINT,
    BREAK,
    CONTINUE

}

public class MyScanner {
    HashMap<Character, TokenType> singleTokenMap = new HashMap<>();
    HashMap<String, TokenType> multiTokenMap = new HashMap<>();
    HashMap<String, TokenType> keywordMap = new HashMap<>();

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
    } //double tokens

    {   keywordMap.put("and", TokenType.AND);
        keywordMap.put("or", TokenType.OR);
        keywordMap.put("if", TokenType.IF);
        keywordMap.put("else", TokenType.ELSE);
        keywordMap.put("while", TokenType.WHILE);
        keywordMap.put("for", TokenType.FOR);
        keywordMap.put("fun", TokenType.FUN);
        keywordMap.put("return", TokenType.RETURN);
        keywordMap.put("var", TokenType.VAR);
        keywordMap.put("class", TokenType.CLASS);
        keywordMap.put("this", TokenType.THIS);
        keywordMap.put("super", TokenType.SUPER);
        keywordMap.put("nil", TokenType.NIL);
        keywordMap.put("true", TokenType.TRUE);
        keywordMap.put("false", TokenType.FALSE);
        keywordMap.put("print", TokenType.PRINT);
        keywordMap.put("break", TokenType.BREAK);
        keywordMap.put("continue", TokenType.CONTINUE);
    } //reserved words

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
        char c = moveNext();
        switch (c) {
            case '(', ')', '{', '}', '.', '*', '-', '+', ',', ';':
                addToken(singleTokenMap.get(c), Character.toString(c), null,this.curLine);
                break;

            //Checking single slash and comments
            case '/':
                if (checkNext() == '/') {
                    //A comment goes until the end of the line
                    while (checkNext() != '\n' && checkNext() != '\0') moveNext();

                } else {
                    addToken(singleTokenMap.get(c), Character.toString(c), null,this.curLine);
                }
                break;

            //checking  for relational operators
            case '=', '<', '>', '!':
                if (checkNext() == '=') {
                    String op = Character.toString(c) + moveNext();
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
                while(checkNext()!='"' && checkNext()!='\0'){
                    if(checkNext()=='\n') {
                    curLine++;
//                    advance();
//                    continue;
                    }

                    str.append(moveNext());
                }
                if(checkNext()=='\0'){
                    System.err.println("[line " + curLine + "] Error: Unterminated string.");
                    errCode = 65;
                }else{
                    moveNext();
                    addToken(TokenType.STRING, '"'+str.toString()+'"', str.toString(),curLine);
                }
                break;

            case '1','2','3','4','5','6','7','8','9','0':
                StringBuilder num = new StringBuilder();
                num.append(c);
                while(Character.isDigit(checkNext())){
                    num.append(moveNext());
                }
                if(checkNext()=='.' && Character.isDigit(source.charAt(current+1))){
                    do {
                        num.append(moveNext());
                    } while (Character.isDigit(checkNext()));
                }

                addToken(TokenType.NUMBER, num.toString(), Double.parseDouble(num.toString()),curLine);
                break;

            //If character not in enum
            default:
                if(Character.isAlphabetic(c)|| c=='_'){
                    StringBuilder id = new StringBuilder();
                    id.append(c);
                    while(Character.isAlphabetic(checkNext()) || Character.isDigit(checkNext())|| checkNext()=='_'){
                        id.append(moveNext());
                    }
                    if(keywordMap.containsKey(id.toString())){

                        addToken(keywordMap.get(id.toString()), id.toString(), null,curLine);
                        break;
                    }
                    addToken(TokenType.IDENTIFIER, id.toString(), null,curLine);
                    break;

                }
                System.err.println("[line " + curLine + "] Error: Unexpected character: " + c);
                errCode = 65;

        }
    }

    void scanSource() {
        while (current < source.length()) {
            scan();
        }
        addToken(TokenType.EOF, "", null,curLine);
    }

    char checkNext() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    char moveNext() {
        if (current >= source.length()) return '\0';
        char cur = source.charAt(current);
        current++;
        return cur;
    }
}

package bhashaforge.lexer;
import bhashaforge.token.Token;
import bhashaforge.token.Token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String code;
    private int pos;
    private int line;
    private List<Token> tokens;

    public Lexer(String code) {
        this.code = code;
        this.pos = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }

    // tokenize everything
    public List<Token> tokenize() {
        while (pos < code.length()) {
            skipWhitespace();
            if (pos >= code.length()) break;

            char c = code.charAt(pos);

            if (c == '\n') {
                line++;
                pos++;
            } else if (c == '#') {
                skipComment();
            } else if (Character.isDigit(c)) {
                readNumber();
            } else if (c == '"') {
                readString();
            } else if (Character.isLetter(c) || c == '_') {
                readWord();
            } else {
                readSymbol(c);
            }
        }
        tokens.add(new Token(TokenType.EOF, "EOF", line));
        return tokens;
    }

    private void skipWhitespace() {
        while (pos < code.length() &&
                (code.charAt(pos) == ' ' || code.charAt(pos) == '\t')) {
            pos++;
        }
    }

    private void skipComment() {
        while (pos < code.length() && code.charAt(pos) != '\n') {
            pos++;
        }
    }

    private void readNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < code.length() &&
                (Character.isDigit(code.charAt(pos)) || code.charAt(pos) == '.')) {
            sb.append(code.charAt(pos));
            pos++;
        }
        tokens.add(new Token(TokenType.NUMBER, sb.toString(), line));
    }

    private void readString() {
        pos++; // skip opening "
        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && code.charAt(pos) != '"') {
            sb.append(code.charAt(pos));
            pos++;
        }
        pos++; // skip closing "
        tokens.add(new Token(TokenType.STRING, sb.toString(), line));
    }

    private void readWord() {
        StringBuilder sb = new StringBuilder();
        while (pos < code.length() &&
                (Character.isLetterOrDigit(code.charAt(pos)) || code.charAt(pos) == '_')) {
            sb.append(code.charAt(pos));
            pos++;
        }
        String word = sb.toString();
        tokens.add(new Token(getKeywordType(word), word, line));
    }

    private TokenType getKeywordType(String word) {
        switch (word) {
            case "dhoro":     return TokenType.DHORO;
            case "dekhao":    return TokenType.DEKHAO;
            case "jodi":      return TokenType.JODI;
            case "nahole":    return TokenType.NAHOLE;
            case "jotokkhon": return TokenType.JOTOKKHON;
            default:          return TokenType.IDENTIFIER;
        }
    }
    
    private void readSymbol(char c) {
        switch (c) {
            case '+': tokens.add(new Token(TokenType.PLUS,     "+", line)); break;
            case '-': tokens.add(new Token(TokenType.MINUS,    "-", line)); break;
            case '*': tokens.add(new Token(TokenType.MULTIPLY, "*", line)); break;
            case '/': tokens.add(new Token(TokenType.DIVIDE,   "/", line)); break;
            case '(': tokens.add(new Token(TokenType.LPAREN,   "(", line)); break;
            case ')': tokens.add(new Token(TokenType.RPAREN,   ")", line)); break;
            case '{': tokens.add(new Token(TokenType.LBRACE,   "{", line)); break;
            case '}': tokens.add(new Token(TokenType.RBRACE,   "}", line)); break;
            case '=':
                // Check if next char is also = (making ==)
                if (pos + 1 < code.length() && code.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.EQEQ, "==", line));
                    pos++; // skip extra =
                } else {
                    tokens.add(new Token(TokenType.EQUALS, "=", line));
                }
                break;
            case '!':
                if (pos + 1 < code.length() && code.charAt(pos + 1) == '=') {
                    tokens.add(new Token(TokenType.NOT_EQ, "!=", line));
                    pos++;
                }
                break;
            case '>': tokens.add(new Token(TokenType.GREATER, ">", line)); break;
            case '<': tokens.add(new Token(TokenType.LESS,    "<", line)); break;
            default:
                System.out.println("⚠️ Unknown character '" + c + "' at line " + line);
        }
        pos++;
    }
}
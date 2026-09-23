package bhashaforge.token;
public class Token {
    public enum TokenType {
        // Keywords (Banglish)
        DHORO,
        DEKHAO,
        JODI,
        NAHOLE,
        JOTOKKHON,

        // Values
        NUMBER,
        STRING,
        IDENTIFIER,

        // Operators
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        EQUALS,
        EQEQ,
        NOT_EQ,
        GREATER,
        LESS,

        // Symbols
        LPAREN,     // (
        RPAREN,     // )
        LBRACE,     // {
        RBRACE,     // }

        // Special
        EOF,        // End of file/code
    }

    // Private fields --> Encapsulation
    private TokenType type;
    private String value;
    private int line;

    // Constructor
    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    // Getters
    public TokenType getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
    public int getLine() {
        return line;
    }

    // Shows token nicely when printed
    @Override
    public String toString() {
        return "[" + type + " : '" + value + "' (line " + line + ")]";
    }
}
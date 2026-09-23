package bhashaforge.parser;
import bhashaforge.ast.*;
import bhashaforge.token.Token;
import bhashaforge.token.Token.TokenType;
import java.util.ArrayList;
import java.util.List;


public class Parser {
    private List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    // Look at current token without moving
    private Token current() {
        return tokens.get(pos);
    }

    // Look at current token AND move forward
    private Token consume() {
        Token t = tokens.get(pos);
        pos++;
        return t;
    }

    private Token expect(TokenType type) {
        Token t = current();
        if (t.getType() != type) {
            throw new RuntimeException(
                    "❌ Syntax Error at line " + t.getLine() +
                            ": Expected " + type +
                            " but got '" + t.getValue() + "'"
            );
        }
        return consume();
    }

    // Check if current token matches type
    private boolean check(TokenType type) {
        return current().getType() == type;
    }

    // Parse ALL statements in the code
    public List<Node> parseAll() {
        List<Node> statements = new ArrayList<>();
        while (!check(TokenType.EOF)) {
            // skip blank lines
            if (pos < tokens.size()) {
                Node stmt = parseStatement();
                if (stmt != null) statements.add(stmt);
            }
        }
        return statements;
    }

    // Parse ONE statement
    private Node parseStatement() {
        Token t = current();

        switch (t.getType()) {
            case DHORO:     return parseAssign();
            case DEKHAO:    return parsePrint();
            case JODI:      return parseIf();
            case JOTOKKHON: return parseWhile();
            default:
                consume();
                return null;
        }
    }

    private Node parseAssign() {
        int line = current().getLine();
        expect(TokenType.DHORO);
        String varName = expect(TokenType.IDENTIFIER)
                .getValue();
        expect(TokenType.EQUALS);
        Node value = parseExpression();
        return new AssignNode(varName, value, line);
    }

    private Node parsePrint() {
        int line = current().getLine();
        expect(TokenType.DEKHAO);
        expect(TokenType.LPAREN);
        Node expr = parseExpression();
        expect(TokenType.RPAREN);
        return new PrintNode(expr, line);
    }

    private Node parseIf() {
        int line = current().getLine();
        expect(TokenType.JODI);
        expect(TokenType.LPAREN);
        Node condition = parseCondition();
        expect(TokenType.RPAREN);
        expect(TokenType.LBRACE);
        List<Node> body = parseBlock();
        expect(TokenType.RBRACE);

        List<Node> elseBody = new ArrayList<>();
        if (check(TokenType.NAHOLE)) {
            consume();               // consume "nahole"
            expect(TokenType.LBRACE);
            elseBody = parseBlock();
            expect(TokenType.RBRACE);
        }

        return new IfNode(condition, body, elseBody, line);
    }

    private Node parseWhile() {
        int line = current().getLine();
        expect(TokenType.JOTOKKHON); // consume "jabotkhon"
        expect(TokenType.LPAREN);
        Node condition = parseCondition();
        expect(TokenType.RPAREN);
        expect(TokenType.LBRACE);
        List<Node> body = parseBlock();
        expect(TokenType.RBRACE);
        return new WhileNode(condition, body, line);
    }

    private List<Node> parseBlock() {
        List<Node> statements = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !check(TokenType.EOF)) {
            Node stmt = parseStatement();
            if (stmt != null) statements.add(stmt);
        }
        return statements;
    }

    private Node parseCondition() {
        int line = current().getLine();
        Node left = parseExpression();

        if (check(TokenType.GREATER) || check(TokenType.LESS) ||
                check(TokenType.EQEQ)    || check(TokenType.NOT_EQ)) {

            String op = consume().getValue();
            Node right = parseExpression();
            return new BinaryOpNode(left, op, right, line);
        }

        return left;
    }

    private Node parseExpression() {
        int line = current().getLine();
        Node left = parseTerm();

        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            String op = consume().getValue();
            Node right = parseTerm();
            left = new BinaryOpNode(left, op, right, line);
        }

        return left;
    }

    private Node parseTerm() {
        int line = current().getLine();
        Node left = parsePrimary();

        while (check(TokenType.MULTIPLY) || check(TokenType.DIVIDE)) {
            String op = consume().getValue();
            Node right = parsePrimary();
            left = new BinaryOpNode(left, op, right, line);
        }

        return left;
    }

    private Node parsePrimary() {
        Token t = current();

        if (t.getType() == TokenType.NUMBER) {
            consume();
            return new NumberNode(Double.parseDouble(t.getValue()), t.getLine());
        }

        if (t.getType() == TokenType.STRING) {
            consume();
            return new StringNode(t.getValue(), t.getLine());
        }

        if (t.getType() == TokenType.IDENTIFIER) {
            consume();
            return new IdentifierNode(t.getValue(), t.getLine());
        }

        if (t.getType() == TokenType.LPAREN) {
            consume();             // skip (
            Node expr = parseExpression();
            expect(TokenType.RPAREN); // skip )
            return expr;
        }

        throw new RuntimeException(
                "❌ Syntax Error at line " + t.getLine() +
                        ": Unexpected token '" + t.getValue() + "'"
        );
    }
}
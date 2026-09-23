package bhashaforge.ast;

public class PrintNode extends Node {

    private Node expression; // what to print

    public PrintNode(Node expression, int line) {
        super(line);
        this.expression = expression;
    }

    public Node getExpression() { return expression; }

    @Override
    public String describe() {
        return "Print(" + expression.describe() + ")";
    }
}
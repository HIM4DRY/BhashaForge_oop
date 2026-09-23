package bhashaforge.ast;

// Represents math operations like x + 5 or y - 2
public class BinaryOpNode extends Node {

    private Node left;
    private String op;
    private Node right;

    public BinaryOpNode(Node left, String op, Node right, int line) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public Node getLeft()  { return left; }
    public String getOp()  { return op; }
    public Node getRight() { return right; }

    @Override
    public String describe() {
        return "BinaryOp(" + left.describe() + " " + op + " " + right.describe() + ")";
    }
}
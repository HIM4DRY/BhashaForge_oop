package bhashaforge.ast;

// Abstract base class for all AST Nodes
public abstract class Node {

    private int line;

    public Node(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    // Every node must implement this
    // Polymorphism: each node describes itself differently
    public abstract String describe();
}
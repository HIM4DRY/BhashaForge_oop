package bhashaforge.ast;

// Represents a variable name like x or y
public class IdentifierNode extends Node {

    private String name;

    public IdentifierNode(String name, int line) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String describe() {
        return "Identifier(" + name + ")";
    }
}
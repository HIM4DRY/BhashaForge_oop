package bhashaforge.ast;

// Represents a string like "hello"
public class StringNode extends Node {

    private String value;

    public StringNode(String value, int line) {
        super(line);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String describe() {
        return "String(\"" + value + "\")";
    }
}
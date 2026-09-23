package bhashaforge.ast;

// Represents a number like 5 or 3.14
public class NumberNode extends Node {

    private double value;

    public NumberNode(double value, int line) {
        super(line);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String describe() {
        return "Number(" + value + ")";
    }
}
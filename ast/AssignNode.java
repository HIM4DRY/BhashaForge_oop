package bhashaforge.ast;

// Represents: dhoro x = 5
public class AssignNode extends Node {

    private String varName;  // x
    private Node value;      // 5

    public AssignNode(String varName, Node value, int line) {
        super(line);
        this.varName = varName;
        this.value = value;
    }

    public String getVarName() { return varName; }
    public Node getValue()     { return value; }

    @Override
    public String describe() {
        return "Assign(" + varName + " = " + value.describe() + ")";
    }
}
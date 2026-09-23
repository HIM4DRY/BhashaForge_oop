package bhashaforge.ast;

import java.util.List;

// Represents: jodi (condition) { body }
public class IfNode extends Node {

    private Node condition;
    private List<Node> body;
    private List<Node> elseBody;

    public IfNode(Node condition, List<Node> body, List<Node> elseBody, int line) {
        super(line);
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    public Node getCondition()      { return condition; }
    public List<Node> getBody()     { return body; }
    public List<Node> getElseBody() { return elseBody; }

    @Override
    public String describe() {
        return "If(" + condition.describe() + ")";
    }
}
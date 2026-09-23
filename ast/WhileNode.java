package bhashaforge.ast;
import java.util.List;

public class WhileNode extends Node {

    private Node condition;
    private List<Node> body;

    public WhileNode(Node condition, List<Node> body, int line) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public Node getCondition()  { return condition; }
    public List<Node> getBody() { return body; }

    @Override
    public String describe() {
        return "While(" + condition.describe() + ")";
    }
}
package bhashaforge.interpreter;
import bhashaforge.ast.*;
import bhashaforge.environment.Environment;
import java.util.List;

public class Interpreter {

    private Environment environment;

    public Interpreter() {
        this.environment = new Environment();
    }

    // Reset variables for fresh run
    public void reset() {
        environment.clear();
    }

    // Execute a list of statements
    public void executeAll(List<Node> statements) {
        for (Node stmt : statements) {
            if (stmt != null) {
                execute(stmt);
            }
        }
    }

    private void execute(Node node) {
        if (node instanceof AssignNode) {
            executeAssign((AssignNode) node);
        } else if (node instanceof PrintNode) {
            executePrint((PrintNode) node);
        } else if (node instanceof IfNode) {
            executeIf((IfNode) node);
        } else if (node instanceof WhileNode) {
            executeWhile((WhileNode) node);
        } else {
            throw new RuntimeException(
                    "❌ Unknown statement at line " + node.getLine()
            );
        }
    }

    private void executeAssign(AssignNode node) {
        String varName = node.getVarName();
        Object value = evaluate(node.getValue());
        environment.set(varName, value);
    }

    private void executePrint(PrintNode node) {
        Object value = evaluate(node.getExpression());
        System.out.println(formatValue(value));
    }

    private void executeIf(IfNode node) {
        Object condResult = evaluate(node.getCondition());
        boolean isTrue = isTruthy(condResult);

        if (isTrue) {
            executeAll(node.getBody());
        } else if (node.getElseBody() != null &&
                !node.getElseBody().isEmpty()) {
            executeAll(node.getElseBody());
        }
    }

    private void executeWhile(WhileNode node) {
        int safetyLimit = 10000;
        int count = 0;

        while (isTruthy(evaluate(node.getCondition()))) {
            executeAll(node.getBody());
            count++;
            if (count >= safetyLimit) {
                System.out.println("⚠️ Loop stopped: too many iterations!");
                break;
            }
        }
    }

    public Object evaluate(Node node) {
        if (node instanceof NumberNode) {
            return ((NumberNode) node).getValue();
        }

        if (node instanceof StringNode) {
            return ((StringNode) node).getValue();
        }

        if (node instanceof IdentifierNode) {
            String name = ((IdentifierNode) node).getName();
            return environment.get(name); // get from Environment
        }

        if (node instanceof BinaryOpNode) {
            return evaluateBinaryOp((BinaryOpNode) node);
        }

        throw new RuntimeException(
                "❌ Cannot evaluate node: " + node.describe()
        );
    }

    private Object evaluateBinaryOp(BinaryOpNode node) {
        Object left  = evaluate(node.getLeft());
        Object right = evaluate(node.getRight());
        String op    = node.getOp();

        if (op.equals("+")) {
            // Special: string + anything = string concat
            if (left instanceof String || right instanceof String) {
                return formatValue(left) + formatValue(right);
            }
            return toNumber(left) + toNumber(right);
        }
        if (op.equals("-")) return toNumber(left) - toNumber(right);
        if (op.equals("*")) return toNumber(left) * toNumber(right);
        if (op.equals("/")) {
            double r = toNumber(right);
            if (r == 0) throw new RuntimeException("❌ Cannot divide by zero!");
            return toNumber(left) / r;
        }

        if (op.equals(">"))  return toNumber(left) > toNumber(right)  ? 1.0 : 0.0;
        if (op.equals("<"))  return toNumber(left) < toNumber(right)  ? 1.0 : 0.0;
        if (op.equals("==")) return left.equals(right)                ? 1.0 : 0.0;
        if (op.equals("!=")) return !left.equals(right)               ? 1.0 : 0.0;

        throw new RuntimeException("❌ Unknown operator: " + op);
    }

    private double toNumber(Object val) {
        if (val instanceof Double) return (Double) val;
        if (val instanceof String) {
            try {
                return Double.parseDouble((String) val);
            } catch (NumberFormatException e) {
                throw new RuntimeException(
                        "❌ Cannot use '" + val + "' as a number!"
                );
            }
        }
        throw new RuntimeException("❌ Not a number: " + val);
    }

    // Check if value is true (non-zero number = true)
    private boolean isTruthy(Object val) {
        if (val instanceof Double) return (Double) val != 0.0;
        if (val instanceof String) return !((String) val).isEmpty();
        return false;
    }

    // Format value for printing
    private String formatValue(Object val) {
        if (val instanceof Double) {
            double d = (Double) val;

            if (d == Math.floor(d)) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(val);
    }

    // Show all current variables
    public void showVariables() {
        System.out.println("📦 Current Variables:");
        environment.printAll();
    }
}
package bhashaforge.environment;
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> variables;

    public Environment() {
        this.variables = new HashMap<>();
    }

    // Save a variable
    public void set(String name, Object value) {
        variables.put(name, value);
    }

    // Get a variable's value
    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException(
                    "❌ Variable '" + name + "' not defined!"
            );
        }
        return variables.get(name);
    }

    // Check if variable exists
    public boolean has(String name) {
        return variables.containsKey(name);
    }

    public void clear() {
        variables.clear();
    }
    
    public void printAll() {
        if (variables.isEmpty()) {
            System.out.println("  (no variables)");
            return;
        }
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
    }
}
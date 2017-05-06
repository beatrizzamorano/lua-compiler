import java.util.HashMap;
import java.util.Objects;

public class Program {
    private HashMap<String, Variable> globalVariables;
    private HashMap<String, Function> functions;

    public Program() {
        this.globalVariables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public void addGlobalVariable(Variable variable) throws SemanthicException {
        for (Function function : functions.values()) {
            if (Objects.equals(function.getName(), variable.getName())) {
                throw new SemanthicException(525);
            }
        }
        this.globalVariables.put(variable.getName(), variable);
    }

    public void addFunction(Function function) throws SemanthicException {
        for (Function declaredFunction : functions.values()) {
            if (Objects.equals(function.getName(), declaredFunction.getName())) {
                throw new SemanthicException(521);
            }
        }

        for (Variable variable : globalVariables.values()) {
            if (Objects.equals(function.getName(), variable.getName())) {
                throw new SemanthicException(524);
            }
        }
        this.functions.put(function.getName(), function);
    }

    public void updateFunction(Function function) {
        this.functions.put(function.getName(), function);
    }

    public void addLocalVariable(String functionName, Variable variable) throws SemanthicException {
        for (Variable globalVariable : globalVariables.values()) {
            if (globalVariable.getName().equals(variable.getName())) {
                throw new SemanthicException(522);
            }
        }

        for (Function function : functions.values()) {
            if (function.getName().equals(variable.getName())) {
                throw new SemanthicException(523);
            }
        }

        Function function = functions.get(functionName);
        function.addLocalVariable(variable);
        functions.put(functionName, function);
    }
}
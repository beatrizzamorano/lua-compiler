package Compiler;

import Statements.Statement;

import java.util.*;

public class Program {
    private static Program instance;

    private HashMap<String, Function> functions;
    private Scope scope;

    private Program(Scope scope) {
        this.functions = new HashMap<>();
        this.scope = scope;
    }

    public static Program getInstance() {
        if (instance == null) {
            instance = new Program(new Scope(1, null));
        }
        return instance;
    }

    public static Program newInstance() {
        instance = new Program(new Scope(1, null));
        return instance;
    }

    public void addGlobalVariable(Variable variable) throws SemanthicException {
        for (Function function : instance.functions.values()) {
            if (Objects.equals(function.getName(), variable.getName())) {
                throw new SemanthicException(525);
            }
        }

        instance.scope.addVariable(variable);
    }

    public Variable getGlobalVariable(Variable variable) {
        return instance.scope.findVariable(variable);
    }

    public void addFunction(Function function) throws SemanthicException {
        for (Function declaredFunction : instance.functions.values()) {
            if (Objects.equals(function.getName(), declaredFunction.getName())) {
                throw new SemanthicException(521);
            }
        }

        if (instance.scope.hasVariableName(function.getName())) {
            throw new SemanthicException(524);
        }

        instance.functions.put(function.getName(), function);
    }

    public void updateFunction(Function function) {
        instance.functions.put(function.getName(), function);
    }

    public void addLocalVariable(int parentScopeId, int scopeId, Variable variable) throws SemanthicException {
        instance.scope.addVariable(parentScopeId, scopeId, variable);
    }
}
package Compiler;

import Statements.GroupAssignStatement;
import Statements.Statement;

import java.util.*;
import java.util.stream.Collectors;

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

    public Variable geVariable(Variable variable) {
        return instance.scope.findVariable(variable);
    }

    public void addFunction(Function function, int scope) throws SemanthicException {
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
        Scope parent = instance.scope.findScope(parentScopeId);
        instance.scope.addVariable(parent, scopeId, variable);
    }

    public void evaluateFunction(Function function) throws SemanthicException {
        List<String> parameterNames = function.getParametersNames();

        List<List<String>> localVariableNames = function.getStatements().stream()
                .filter(s -> s instanceof GroupAssignStatement)
                .map(s -> (GroupAssignStatement) s)
                .filter(groupAssignStatement -> groupAssignStatement.isLocal())
                .map(g -> g.getVariableNames())
                .collect(Collectors.toList());

        for (List<String> variables : localVariableNames) {
            for (String variableName : variables) {
                boolean hasAny = parameterNames.stream().anyMatch(s -> Objects.equals(variableName, s));
                if (hasAny) {
                    throw new SemanthicException(533);
                }
            }
        }
    }
}
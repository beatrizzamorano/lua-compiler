package Compiler;

import Statements.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class Program {
    private HashMap<String, Variable> globalVariables;
    private HashMap<String, Function> functions;

    Program() {
        this.globalVariables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    void addGlobalVariable(Variable variable) throws SemanthicException {
        for (Function function : functions.values()) {
            if (Objects.equals(function.getName(), variable.getName())) {
                throw new SemanthicException(525);
            }
        }

        if (globalVariables.containsKey(variable.getName())) {
            Variable currentVariable = globalVariables.get(variable.getName());
            Variable newProperty = (Variable) variable.getProperties().values().toArray()[0];

            while (currentVariable.getProperties().containsKey(newProperty.getName()) && !newProperty.getProperties().isEmpty()) {
                currentVariable = currentVariable.getProperties().get(newProperty.getName());
                newProperty = (Variable) newProperty.getProperties().values().toArray()[0];
            }

            currentVariable.addProperty(newProperty);

        } else {
            this.globalVariables.put(variable.getName(), variable);
        }
    }

    void addFunction(Function function) throws SemanthicException {
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

    void updateFunction(Function function) {
        this.functions.put(function.getName(), function);
    }

    void addLocalVariable(String functionName, Variable variable) throws SemanthicException {
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
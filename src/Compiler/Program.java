package Compiler;

import Statements.Statement;

import java.util.*;

public class Program {
    private static Program instance;

    private HashMap<String, Variable> globalVariables;
    private HashMap<String, Function> functions;

    private Program() {
        this.globalVariables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public static Program getInstance() {
        if (instance == null) {
            instance = new Program();
        }
        return instance;
    }

    public static Program newInstance() {
        instance = new Program();
        return instance;
    }

    public void addGlobalVariable(Variable variable) throws SemanthicException {
        for (Function function : instance.functions.values()) {
            if (Objects.equals(function.getName(), variable.getName())) {
                throw new SemanthicException(525);
            }
        }

        if (instance.globalVariables.containsKey(variable.getName())) {
            Variable currentVariable = instance.globalVariables.get(variable.getName());
            HashMap<String, Variable> properties = variable.getProperties();

            if (!properties.isEmpty()) {
                Variable newProperty = (Variable) variable.getProperties().values().toArray()[0];

                while (currentVariable.getProperties().containsKey(newProperty.getName()) && !newProperty.getProperties().isEmpty()) {
                    currentVariable = currentVariable.getProperties().get(newProperty.getName());
                    newProperty = (Variable) newProperty.getProperties().values().toArray()[0];
                }

                currentVariable.addProperty(newProperty);
            }
        }
        else {
            if (!variable.getProperties().isEmpty()) {
                variable.setType(TypeEnum.TABLE);
            }
            instance.globalVariables.put(variable.getName(), variable);
        }
    }

    public Variable getGlobalVariable(Variable variable) {
        Variable currentVariable = instance.globalVariables.get(variable.getName());
        Variable propertyToGet = currentVariable;

        if (propertyToGet == null) return null;

        while (!variable.getProperties().isEmpty() && !currentVariable.getProperties().isEmpty() && currentVariable.getProperties().containsKey(((Variable) variable.getProperties().values().toArray()[0]).getName())) {
            propertyToGet = currentVariable.getProperties().get(((Variable) variable.getProperties().values().toArray()[0]).getName());
            variable = (Variable) variable.getProperties().values().toArray()[0];
        }

        return propertyToGet;
    }

    public void addFunction(Function function) throws SemanthicException {
        for (Function declaredFunction : instance.functions.values()) {
            if (Objects.equals(function.getName(), declaredFunction.getName())) {
                throw new SemanthicException(521);
            }
        }

        for (Variable variable : instance.globalVariables.values()) {
            if (Objects.equals(function.getName(), variable.getName())) {
                throw new SemanthicException(524);
            }
        }
        instance.functions.put(function.getName(), function);
    }

    public void updateFunction(Function function) {
        instance.functions.put(function.getName(), function);
    }

    public void addLocalVariable(String functionName, Variable variable) throws SemanthicException {
        for (Variable globalVariable : instance.globalVariables.values()) {
            if (globalVariable.getName().equals(variable.getName())) {
                throw new SemanthicException(522);
            }
        }

        for (Function function : instance.functions.values()) {
            if (function.getName().equals(variable.getName())) {
                throw new SemanthicException(523);
            }
        }

        Function function = instance.functions.get(functionName);
        function.addLocalVariable(variable);
        instance.functions.put(functionName, function);
    }
}
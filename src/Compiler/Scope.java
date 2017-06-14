package Compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scope {
    private int id;
    private Scope parentScope;
    private HashMap<String, Variable> variables;
    private List<Scope> childScopes;

    public Scope(int scopeId, Scope parentScope) {
        this.id = scopeId;
        this.parentScope = parentScope;
        this.variables = new HashMap<String, Variable>();
        this.childScopes = new ArrayList<Scope>();
    }

    public int getId() {
        return this.id;
    }

    public void addVariable(Scope parentScope, int scopeId, Variable variable) throws SemanthicException {
        Scope existingScope;

        if (scopeId == this.id) {
            existingScope = this;
        } else {
            existingScope = findScope(scopeId);
        }

        if (existingScope != null) {
            if (existingScope.variables.get(variable.getName()) != null) {
                throw new SemanthicException(532);
            }
            existingScope.addVariable(variable);
        } else {
            Scope newScope = new Scope(scopeId, parentScope);
            newScope.addVariable(variable);
            parentScope.childScopes.add(newScope);
        }
    }

    public void addVariable(Variable variable) {
        if (this.variables.containsKey(variable.getName())) {
            Variable currentVariable = this.variables.get(variable.getName());
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
            this.variables.put(variable.getName(), variable);
        }
    }

    public Scope getParentScope() {
        return this.parentScope;
    }

    public Variable findVariable(Variable variable) {
        Scope currentScope;
        Variable currentVariable;
        Variable propertyToGet;

        if (this.id == variable.getScope()) {
            currentScope = this;
        } else {
            currentScope = findScope(variable.getScope());
        }

        currentVariable = currentScope.variables.get(variable.getName());
        propertyToGet = currentVariable;

        while (propertyToGet == null && currentScope.getParentScope() != null) {
            currentScope = currentScope.getParentScope();
            propertyToGet = currentScope.variables.get(variable.getName());
        }

        while (!variable.getProperties().isEmpty() && !currentVariable.getProperties().isEmpty() && currentVariable.getProperties().containsKey(((Variable) variable.getProperties().values().toArray()[0]).getName())) {
            propertyToGet = currentVariable.getProperties().get(((Variable) variable.getProperties().values().toArray()[0]).getName());
            variable = (Variable) variable.getProperties().values().toArray()[0];
        }

        return propertyToGet;
    }

    public boolean hasVariableName(String variableName) {
        boolean hasVariableName;
        Variable variable = variables.get(variableName);
        Scope currentScope = this;

        if (variable != null) {
            return true;
        } else {
            while (currentScope.getParentScope() != null) {
                currentScope = currentScope.getParentScope();
                hasVariableName = currentScope.hasVariableName(variableName);

                if (hasVariableName) {
                    return true;
                }
            }
        }

        return false;
    }

    public Scope findScope(int scopeId) {
        if (this.id == scopeId) {
            return this;
        }
        for (Scope childScope : childScopes) {
            if (childScope.getId() == scopeId) {
                return childScope;
            }

            Scope scopeSearch = childScope.findScope(scopeId);
            if (scopeSearch != null) {
                return scopeSearch;
            }
        }

        return null;
    }
}

package Compiler;

import Expressions.Node;

import java.util.HashMap;
import java.util.Queue;

/**
 * Created by beatrizzamorano on 3/12/17.
 */
public class Variable implements Node {
    private int id;
    private TypeEnum type;
    private String name;
    private HashMap<String, Variable> properties;
    private int scope;

    public Variable(String name, int scope) {
        this.name = name;
        this.properties = new HashMap<>();
        this.type = TypeEnum.NIL;
        this.id = hashCode();
        this.scope = scope;
    }

    public Variable(Queue<String> variableConstruct, int scope) {
        this.name = variableConstruct.poll();
        this.properties = new HashMap<>();
        this.id = hashCode();
        this.type = TypeEnum.NIL;
        this.scope = scope;

        Variable currentNode = this;

        while(variableConstruct.peek() != null) {
            Variable variable = new Variable(variableConstruct.poll(), scope);
            currentNode.addProperty(variable);
            currentNode = variable;
        }

    }

    public Variable(TypeEnum type) {
        this.type = type;
    }

    public TypeEnum getType() {
        return this.type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void addProperty(Variable property) {
        this.properties.put(property.getName(), property);
    }

    public HashMap<String, Variable> getProperties() {
        return this.properties;
    }

}

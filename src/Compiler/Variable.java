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

    public Variable(String name) {
        this.name = name;
        this.properties = new HashMap<>();
        this.type = TypeEnum.NIL;
        this.id = hashCode();
    }

    public Variable(Queue<String> variableConstruct) {
        this.name = variableConstruct.poll();
        this.properties = new HashMap<>();
        this.id = hashCode();
        this.type = TypeEnum.NIL;
        Variable currentNode = this;

        while(variableConstruct.peek() != null) {
            Variable variable = new Variable(variableConstruct.poll());
            currentNode.addProperty(variable);
            currentNode = variable;
        }

    }

    public Variable(TypeEnum type) {
        this.type = type;
    }

    public Variable(String name, TypeEnum type) {
        this.name = name;
        this.type = type;
        this.id = this.hashCode();
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

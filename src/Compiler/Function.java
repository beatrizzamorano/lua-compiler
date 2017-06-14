package Compiler;

import Statements.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by beatriz zamorano on 21/03/17.
 */
public class Function {
    private HashMap<Integer, Parameter> parameters;
    private boolean hasReturnValue;
    private String name;
    private List<Statement> statements;

    public Function() {
        this.parameters = new HashMap<>();
        this.hasReturnValue = false;
        this.statements = new ArrayList<>();
    }

    public void setHasReturnValue(boolean hasReturnValue) {
        this.hasReturnValue = hasReturnValue;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addParameters(List<Parameter> params) {
        for (Parameter param : params) {
            this.parameters.put(param.hashCode(), param);
        }
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    public List<String> getParametersNames() {
        return parameters.values().stream().map(p -> p.getName()).collect(Collectors.toList());
    }
}

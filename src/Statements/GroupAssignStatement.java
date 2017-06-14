package Statements;

import java.util.ArrayList;
import java.util.List;
import Compiler.*;
import Expressions.Node;

public class GroupAssignStatement implements Statement {
    List<AssignStatement> assignStatements;
    boolean isLocal;

    public GroupAssignStatement(List<Variable> variables, List<List<Node>> expressions, boolean isLocal) {
        this.assignStatements = new ArrayList<>();
        this.isLocal = isLocal;

        for (int i = 0; i < variables.size(); i++) {
            this.assignStatements.add(new AssignStatement(variables.get(i), expressions.get(i)));
        }
    }

    public List<String> getVariableNames() {
        ArrayList<String> variables = new ArrayList<>();
        for (AssignStatement statement : assignStatements) {
            variables.add(statement.getVariableName());
        }

        return variables;
    }

    public boolean isLocal() {
        return isLocal;
    }

    @Override
    public void evaluate() throws SemanthicException {
        for (AssignStatement assignStatement : assignStatements) {
            assignStatement.evaluate();
        }
    }
}

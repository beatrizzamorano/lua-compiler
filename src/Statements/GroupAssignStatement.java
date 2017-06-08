package Statements;

import java.util.ArrayList;
import java.util.List;
import Compiler.*;
import Expressions.Node;

public class GroupAssignStatement implements Statement {
    List<AssignStatement> assignStatements;

    public GroupAssignStatement(List<Variable> variables, List<List<Node>> expressions) {
        this.assignStatements = new ArrayList<>();

        for (int i = 0; i < variables.size(); i++) {
            this.assignStatements.add(new AssignStatement(variables.get(i), expressions.get(i)));
        }
    }

    @Override
    public void evaluate() throws SemanthicException {
    }
}

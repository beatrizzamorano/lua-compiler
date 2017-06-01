package Statements;

import java.util.ArrayList;
import java.util.List;
import Compiler.Variable;
import Compiler.Token;

/**
 * Created by nsl-malvarado on 6/1/17.
 */
public class GroupAssignStatement implements Statement {
    List<AssignStatement> assignStatements;

    public GroupAssignStatement(List<Variable> variables, List<List<Token>> expressions) {
        this.assignStatements = new ArrayList<>();

        for (int i = 0; i < variables.size(); i++) {
            this.assignStatements.add(new AssignStatement(variables.get(i), expressions.get(i)));
        }
    }
}

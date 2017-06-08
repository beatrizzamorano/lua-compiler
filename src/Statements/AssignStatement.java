package Statements;

import Expressions.*;
import Compiler.Variable;
import Compiler.*;

import java.util.ArrayList;
import java.util.List;

public class AssignStatement implements Statement {
    ASTNode syntaxTree;
    ShuntingYardParser parser;
    ASTEvaluator evaluator = new ASTEvaluator();

    public AssignStatement(Variable variable, List<Node> expression) {
        parser = new ShuntingYardParser();
        OperatorFactory operatorFactory = new OperatorFactory();
        BaseOperator assignOperator = operatorFactory.getOperator(new Token("=", 115, 0));

        List<Node> assign = new ArrayList<>();
        assign.add(variable);
        assign.add(assignOperator);
        assign.addAll(expression);

        this.syntaxTree = parser.convertInfixNotationToAST(assign);
    }

    @Override
    public void evaluate() throws SemanthicException {
        evaluator.evaluateAST(syntaxTree);
    }
}

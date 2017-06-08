package Statements;

import Compiler.*;
import Expressions.ASTEvaluator;
import Expressions.ASTNode;
import Expressions.Node;
import Expressions.ShuntingYardParser;

import java.util.List;

public class ForStatement implements Statement {

    private AssignStatement assignStatement;
    private ASTNode condition;
    private ASTNode cycleExpression;
    private List<Statement> cycleStatements;
    private ShuntingYardParser parser;
    private ASTEvaluator evaluator = new ASTEvaluator();

    public ForStatement(AssignStatement assignStatement, List<Node> conditionExpression) {
        this.parser = new ShuntingYardParser();

        this.assignStatement = assignStatement;
        this.condition = parser.convertInfixNotationToAST(conditionExpression);
    }

    public void setCycleExpression(List<Node> cycleExpression) {
        this.cycleExpression = parser.convertInfixNotationToAST(cycleExpression);
    }

    public void setCycleStatements(List<Statement> statements) {
        this.cycleStatements = statements;
    }

    @Override
    public void evaluate() throws SemanthicException {
        TypeEnum conditionType = evaluator.evaluateAST(condition);
        if (conditionType != TypeEnum.BOOLEAN) throw new SemanthicException(530);

        assignStatement.evaluate();

        for (Statement cycleStatement : cycleStatements) {
            cycleStatement.evaluate();
        }
    }
}

package Statements;

import Compiler.*;
import Expressions.ASTEvaluator;
import Expressions.ASTNode;
import Expressions.Node;
import Expressions.ShuntingYardParser;

import java.util.ArrayList;
import java.util.List;

public class IfStatement implements Statement {

    private ASTNode firstConditional;
    private List<Statement> statements;
    private List<ElseIfStatement> elseIfStatements;
    private List<Statement> elseStatements;
    private ShuntingYardParser parser;

    private ASTEvaluator evaluator = new ASTEvaluator();

    public IfStatement(List<Node> expression) {
        parser = new ShuntingYardParser();
        elseIfStatements = new ArrayList<>();
        this.firstConditional = parser.convertInfixNotationToAST(expression);
    }

    public void addElseIfStatement(ElseIfStatement elseIfStatement) {
        this.elseIfStatements.add(elseIfStatement);
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public void setElseStatements(List<Statement> statements) {
        this.elseStatements = statements;
    }

    @Override
    public void evaluate() throws SemanthicException {
        TypeEnum type = evaluator.evaluateAST(firstConditional);
        if (type != TypeEnum.BOOLEAN) throw new SemanthicException(529);

        for (Statement statement : statements) {
            statement.evaluate();
        }

        for (ElseIfStatement elseIfStatement : elseIfStatements) {
            elseIfStatement.evaluate();
        }

        for (Statement statement : elseStatements) {
            statement.evaluate();
        }
    }
}

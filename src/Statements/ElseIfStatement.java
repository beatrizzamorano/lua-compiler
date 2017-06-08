package Statements;

import Expressions.ASTEvaluator;
import Expressions.ASTNode;

import java.lang.reflect.Type;
import java.util.List;
import Compiler.*;
import Expressions.Node;
import Expressions.ShuntingYardParser;

public class ElseIfStatement implements Statement {

    ASTNode condition;
    List<Statement> statements;
    ShuntingYardParser parser;
    ASTEvaluator evaluator = new ASTEvaluator();

    public ElseIfStatement(List<Node> expression, List<Statement> statements) {
        this.parser = new ShuntingYardParser();

        this.condition = parser.convertInfixNotationToAST(expression);
        this.statements = statements;
    }

    @Override
    public void evaluate() throws SemanthicException {
        TypeEnum type = evaluator.evaluateAST(condition);
        if (type != TypeEnum.BOOLEAN) throw new SemanthicException(529);

        for (Statement statement : statements) {
            statement.evaluate();
        }
    }
}

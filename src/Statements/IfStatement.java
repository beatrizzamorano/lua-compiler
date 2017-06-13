package Statements;

import Compiler.*;
import Expressions.ASTEvaluator;
import Expressions.ASTNode;
import Expressions.Node;
import Expressions.ShuntingYardParser;
import Parsing.ASTParser;
import Parsing.IParse;

import java.util.ArrayList;
import java.util.List;

public class IfStatement implements Statement, IParse {

    private ASTNode firstConditional;
    private List<Statement> statements;
    private List<ElseIfStatement> elseIfStatements;
    private List<Statement> elseStatements;
    private ShuntingYardParser parser;
    private int elseIfIndex = 0;

    private ASTEvaluator evaluator = new ASTEvaluator();

    public IfStatement(List<Node> expression) {
        parser = new ShuntingYardParser();
        elseIfStatements = new ArrayList<>();
        elseStatements = new ArrayList<>();
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

    @Override
    public String parse(int index) {
        String pCode = ASTParser.parseAST(firstConditional);
        pCode += "FJ ELSE_" + index + "\n";

        for (Statement statement : statements) {
            if (statement instanceof IParse) {
                IParse expression = (IParse) statement;
                pCode += expression.parse(index);
            }
        }

        pCode += "UJP END_IF_" + index + "\n";

        for (ElseIfStatement elseIfStatement : elseIfStatements) {
            elseIfStatement.addIfIndex(index);
            pCode += elseIfStatement.parse(elseIfIndex);
        }

        pCode += "LAB ELSE_" + index + "\n";

        for (Statement elseStatement : elseStatements) {
            if (elseStatement instanceof IParse) {
                IParse expression = (IParse) elseStatement;
                pCode += expression.parse(index);
            }
        }

        pCode += "LAB END_IF_" + index + "\n";

        return pCode;
    }
}

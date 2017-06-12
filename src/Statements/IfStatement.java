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
    public String parse() {
        String pCode = ASTParser.parseAST(firstConditional);
        pCode += "FJ ELSE\n";

        for (Statement statement : statements) {
            if (statement instanceof AssignStatement || statement instanceof IfStatement) {
                IParse expression = (IParse) statement;
                pCode += expression.parse();
            }
        }

        pCode += "UJP END_IF\n";

        for (ElseIfStatement elseIfStatement : elseIfStatements) {
            pCode += elseIfStatement.parse();
        }

        pCode += "LAB ELSE\n";

        for (Statement elseStatement : elseStatements) {
            if (elseStatement instanceof AssignStatement || elseStatement instanceof IfStatement) {
                IParse expression = (IParse) elseStatement;
                pCode += expression.parse();
            }
        }

        pCode += "LAB END_IF\n";

        return pCode;
    }
}

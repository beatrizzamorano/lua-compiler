package Statements;

import Expressions.ASTEvaluator;
import Expressions.ASTNode;

import java.util.List;
import Compiler.*;
import Expressions.Node;
import Expressions.ShuntingYardParser;
import Parsing.ASTParser;
import Parsing.IParse;

public class ElseIfStatement implements Statement, IParse {

    ASTNode condition;
    List<Statement> statements;
    ShuntingYardParser parser;
    ASTEvaluator evaluator = new ASTEvaluator();
    int ifIndex = 0;
    int elseIfIndex = 0;

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

    public void addIfIndex(int index) {
        this.ifIndex = index;
    }

    @Override
    public String parse(int index) {
        String pCode = "";

        pCode += "LAB ELSE_IF_" + ifIndex + "_" + elseIfIndex + "\n";
        pCode += ASTParser.parseAST(condition);

        for (int i = 0; i < statements.size(); i++) {
            if (i < statements.size() - 1) {
                pCode += "FJ ELSE_IF_" + ifIndex + "_" + elseIfIndex + "\n";
            } else if (i == statements.size() - 1) {
                pCode += "FJ ELSE_" + ifIndex + "\n";
            }

            Statement statement = statements.get(i);

            if (statement instanceof IParse) {
                IParse expression = (IParse) statement;
                pCode += expression.parse(index);
            }
        }

        pCode += "UJP END_IF_" + ifIndex + "\n";

        return pCode;
    }
}

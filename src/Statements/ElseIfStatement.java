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

    @Override
    public String parse() {
        String pCode = "";

        pCode += "LAB NEXT_ELSE_IF\n";
        pCode += ASTParser.parseAST(condition);

        for (int i = 0; i < statements.size(); i++) {
            if (i < statements.size() - 1) {
                pCode += "FJ NEXT_ELSE_IF\n";
            } else if (i == statements.size() - 1) {
                pCode += "FJ ELSE\n";
            }

            Statement statement = statements.get(i);

            if (statement instanceof AssignStatement || statement instanceof IfStatement) {
                IParse expression = (IParse) statement;
                pCode += expression.parse();
            }
        }

        pCode += "UJP END_IF\n";

        return pCode;
    }
}

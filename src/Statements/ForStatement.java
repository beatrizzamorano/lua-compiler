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

public class ForStatement implements Statement, IParse {

    private AssignStatement assignStatement;
    private ASTNode condition;
    private AssignStatement cycleExpression;
    private List<Statement> cycleStatements;
    private ShuntingYardParser parser;
    private ASTEvaluator evaluator = new ASTEvaluator();

    public ForStatement(AssignStatement assignStatement, List<Node> conditionExpression) {
        this.parser = new ShuntingYardParser();
        this.cycleStatements = new ArrayList<>();

        this.assignStatement = assignStatement;
        this.condition = parser.convertInfixNotationToAST(conditionExpression);
    }

    public void setCycleExpression(AssignStatement cycleExpression) {
        this.cycleExpression = cycleExpression;
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

    @Override
    public String parse(int index) {
        String pCode = "";

        pCode += assignStatement.parse(index);
        pCode += "LAB FOR_CONDITION_" + index + "\n";
        pCode += ASTParser.parseAST(condition);
        pCode += "FJ END_FOR_" + index + "\n";

        for (Statement statement : cycleStatements) {
            if (statement instanceof IParse) {
                IParse sentenceToParse = (IParse) statement;
                pCode += sentenceToParse.parse(index);
            }
        }

        pCode += cycleExpression.parse(index);

        pCode += "UJP FOR_CONDITION_" + index + "\n";
        pCode += "LAB END_FOR_" + index + "\n";

        return pCode;
    }
}

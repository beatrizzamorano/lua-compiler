package Statements;

import Expressions.ASTNode;

import java.util.ArrayList;
import java.util.List;
import Compiler.Token;
import Expressions.ShuntingYardParser;

/**
 * Created by nsl-malvarado on 6/1/17.
 */
public class IfStatement implements Statement {
    private ASTNode firstConditional;
    private List<Statement> statements;
    private List<ElseIfStatement> elseIfStatements;
    private List<Statement> elseStatements;
    private ShuntingYardParser parser;

    public IfStatement(List<Token> expression) {
        parser = new ShuntingYardParser();
        elseIfStatements = new ArrayList<>();
        this.firstConditional = parser.convertInfixNotationToAST(expression);
    }

    public void addFirstConditional(ASTNode conditional) {
        this.firstConditional = conditional;
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
}

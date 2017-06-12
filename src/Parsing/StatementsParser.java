package Parsing;


import Statements.AssignStatement;
import Statements.IfStatement;
import Statements.Statement;

import java.util.List;

public class StatementsParser {
    private List<Statement> statements;
    private String pCode;

    public StatementsParser(List<Statement> statements) {
        this.statements = statements;
        this.pCode = "";
    }

    public String parse() {
        for (Statement statement : statements) {
            if (statement instanceof AssignStatement || statement instanceof IfStatement) {
                IParse sentenceToParse = (IParse) statement;
                pCode += sentenceToParse.parse();
            }


        }

        return this.pCode;
    }

}

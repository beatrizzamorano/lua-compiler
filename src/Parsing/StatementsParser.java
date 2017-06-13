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
        if (statements == null) return "";
        for (Statement statement : statements) {
            if (statement instanceof IParse) {
                IParse sentenceToParse = (IParse) statement;
                pCode += sentenceToParse.parse();
            }
        }

        return this.pCode;
    }

}

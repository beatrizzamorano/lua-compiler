package Compiler;

import Expressions.Node;
import Expressions.OperatorFactory;
import Expressions.ValueNode;
import Parsing.StatementsParser;
import Statements.*;
import javax.swing.*;
import java.util.*;

class SyntacticalAnalysis {
    private ListIterator<Token> iterator;
    private Token currentToken;
    private String errorStack = "";
    private Program program;
    private boolean hasReturnValue;
    private Function function;
    private Queue<String> variableConstruct;
    String pCode = "";

    SyntacticalAnalysis(List<Token> tokens) {
        this.iterator = tokens.listIterator();
        program = Program.newInstance();

        getNextToken();
        List<Statement> block = null;

        try {
            block = isBlock(0, 1);
        } catch (SemanthicException ex) {
            printError(ex.getErrorNumber());
        }

        if (block != null) {
            for (Statement statement : block) {
                try {
                    statement.evaluate();
                } catch (SemanthicException ex) {
                    printError(ex.getErrorNumber());
                }
            }
        }

        StatementsParser parser = new StatementsParser(block);
        String parsedCode = parser.parse();
        pCode = parsedCode;

        if (block != null && errorStack.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sintaxis correcta");
        } else {
            JOptionPane.showMessageDialog(null, errorStack);
        }
    }

    public String getPCode() {
        return this.pCode;
    }

    private void printError(int error){
        if (currentToken != null) {
            this.errorStack += "Error " + error + " cerca de '" + currentToken.lexeme + "' linea "
                    + currentToken.lineNumber + ": " + getErrorMessage(error) + "\n";
        } else {
            this.errorStack += "Error " + error + ": " + getErrorMessage(error) + "\n";
        }
    }

    private String getErrorMessage(int errorNumber){
        switch(errorNumber){

            case 503 : return " '=' expected.";
            case 504 : return " ']' expected.";
            case 505 : return " '}' expected.";
            case 506 : return " ',' expected.";
            case 507 : return " ')' expected.";
            case 508 : return " ':' expected.";
            case 509 : return " '[' expected.";
            case 510 : return " '.' expected.";
            case 511 : return " 'Name' expected.";
            case 512 : return " '...' expected.";
            case 513 : return " 'End' expected.";
            case 514 : return " 'Binop' expected.";
            case 515 : return " 'Do' expected.";
            case 516 : return " 'Until' expected.";
            case 517 : return " 'then' expected.";
            case 518 : return " 'In' expected.";
            case 519 : return " 'Compiler.Function' expected.";
            case 520 : return " '(' expected.";
            case 521 : return "Compiler.Function already declared";
            case 522 : return "Tried to declare a local variable with the name of an existing global variable.";
            case 523 : return "Tried to declare a local variable with the name of an existing function.";
            case 524 : return "Tried to declare a function with the name of an existing global variable.";
            case 525 : return "Tried to declare a global variable with the name of an existing function.";
            case 526 : return "Tried to evaluate a non-supported value.";
            case 527 : return "Nothing to compare in expression";
            case 528 : return "Incompatible types";
            case 529 : return "If statement expects a boolean expression";
            case 530 : return "For statement expects a boolean expression";

            default : return "";
        }

    }

    private Token isBinop() {
        Token binop;

        binop = isPlus();
        if (binop != null) return binop;

        binop = isMinus();
        if (binop != null) return binop;

        binop = isAsterisk();
        if (binop != null) return binop;

        binop = isSlash();
        if (binop != null) return binop;

        binop = isCircumflexAccent();
        if (binop != null) return binop;

        binop = isPercent();
        if (binop != null) return binop;

        binop = isDoubleDot();
        if (binop != null) return binop;

        binop = isLessThan();
        if (binop != null) return binop;

        binop = isLessOrEqualThan();
        if (binop != null) return binop;

        binop = isGreaterThan();
        if (binop != null) return binop;

        binop = isGreaterOrEqualThan();
        if (binop != null) return binop;

        binop = isEqual();
        if (binop != null) return binop;

        binop = isDifferent();
        if (binop != null) return binop;

        binop = isAnd();
        if (binop != null) return binop;

        binop = isOr();
        if (binop != null) return binop;

        return null;
    }

    private Token isUnop(){
        Token unop;

        unop = isMinus();
        if (unop != null) return unop;

        unop = isNot();
        if (unop != null) return unop;

        unop = isHash();
        if (unop != null) return unop;

        return null;
    }

    private boolean isFieldSep(){
        return isComma() || isSemicolon();
    }

    //block ::= chunk
    private List<Statement> isBlock(int scope, int parentScope) throws SemanthicException {
        scope++;
        return isChunk(scope, parentScope);
    }

    //chunk ::= {stat [`;´]} [laststat [`;´]]
    private List<Statement> isChunk(int scope, int parentScope) throws SemanthicException {
        List<Statement> statements = new ArrayList<>();

        try {

            Statement statement = isStat(scope, parentScope);
            while (statement != null) {
                statements.add(statement);
                isSemicolon();

                statement = isStat(scope, parentScope);
            }

            if (isLastStat(scope)) {
                isSemicolon();
            }

            return statements.size() > 0 ? statements : null;

        } catch (NullPointerException ex) {
            return statements.size() > 0 ? statements : null;
        }
    }

    /*stat ::=
         varlist `=´ explist |
		 functioncall |
		 if exp then block {elseif exp then block} [else block] end |
		 for Name `=´ exp `,´ exp [`,´ exp] do block end |
		 function funcname funcbody |
		 local namelist [`=´ explist] */
    private Statement isStat(int scope, int parentScope) throws SemanthicException {
        List<Variable> variables = isVarList(scope);

        if (variables != null && variables.size() > 0) {
            for (Variable variable : variables) {
                try {
                    program.addGlobalVariable(variable);

                } catch (SemanthicException ex) {
                    printError(ex.getErrorNumber());
                }
            }

            if (variables.size() > 1) {
                if (isAssign() != null) {
                    List<List<Node>> expressions = isExpList(scope);
                    if (expressions != null) {
                        if (variables.size() != expressions.size()) return null;

                        return new GroupAssignStatement(variables, expressions);
                    }
                } else {
                    printError(503);
                }
            } else if (variables.size() == 1) {
//                if (isArgs()) {
//                    if (isFunctionCallTail(scope)) {
//                        return true;
//                    }
//                }
//                else if (isColon()) {
//                    if (isName()) {
//                        getNextToken();
//                        if (isArgs()) {
//                            if (isFunctionCallTail(scope)) {
//                                return true;
//                            }
//                        }
//                    }
//                }
                if (isAssign() != null) {
                    List<List<Node>> expressions = isExpList(scope);
                    if (expressions != null) {
                        return new AssignStatement(variables.get(0), expressions.get(0));
                    }
                }

                return new GenericStatement();
            }
        } else if (isFunctionCall(scope)) {
            return new GenericStatement();
        }
        else if (isIf()) {
            List<Node> expression = isExp(scope);
            if (expression != null) {
                IfStatement ifStatement = new IfStatement(expression);

                if (isThen()) {
                    List<Statement> firstConditionalStatements = isBlock(scope, scope);
                    if (firstConditionalStatements != null) {
                        ifStatement.setStatements(firstConditionalStatements);

                        while (isElseIf()) {
                            List<Node> elseIfExpression = isExp(scope);

                            if (elseIfExpression != null) {
                                if (isThen()) {
                                    List<Statement> block = isBlock(scope, scope);
                                    if (block != null) {
                                        ifStatement.addElseIfStatement(new ElseIfStatement(elseIfExpression, block));
                                    } else {
                                        return null;
                                    }
                                } else {
                                    printError(517);
                                }
                            }
                        }
                        if (isElse()) {
                            List<Statement> block = isBlock(scope, scope);
                            if (block != null) {
                                ifStatement.setElseStatements(block);
                            } else {
                                return null;
                            }
                        }
                       if (isEnd()) {
                           return ifStatement;
                       } else {
                           printError(513);
                           return null;
                       }
                    }
                } else {
                    printError(517);
                }
            }
        }
        else if (isFor()) {
            List<String> names = isNameList();

            if (names != null && names.size() == 1) {
                if (isAssign() != null) {
                    List<Node> assignExpression = isExp(scope);

                    if (assignExpression != null) {
                        if (isComma()) {
                            List<Node> conditionExpression = isExp(scope);

                            if (conditionExpression != null) {
                                Variable variable = new Variable(names.get(0), scope);
                                AssignStatement assignStatement = new AssignStatement(variable, assignExpression);

                                program.addGlobalVariable(variable);
                                ForStatement forStatement = new ForStatement(assignStatement, conditionExpression);

                                if (isComma()) {
                                    List<String> names2 = isNameList();
                                    if (names2 != null && names2.size() == 1) {
                                        if (isAssign() != null) {
                                            List<Node> assignExpression2 = isExp(scope);
                                            Variable variable2 = new Variable(names.get(0), scope);
                                            program.addGlobalVariable(variable2);
                                            forStatement.setCycleExpression(new AssignStatement(variable2, assignExpression2));

                                            if (isDo()) {
                                                List<Statement> statements = isBlock(scope, scope);

                                                if (statements != null) {
                                                    forStatement.setCycleStatements(statements);

                                                    if (isEnd()) {
                                                        return forStatement;
                                                    } else {
                                                        printError(513);
                                                    }
                                                }
                                            } else {
                                                printError(515);
                                            }
                                        }
                                    }

                                } else {
                                    printError(515);
                                }
                            }
                        } else {
                            printError(506);
                            return null;
                        }
                    }
                } else {
                    printError(518);
                }
            } else {
                printError(511);
                return null;
            }

        }
        else if (isFunctionKeyword()) {
            if (isFuncName()) {
                if (isFuncBody(scope, parentScope)) {
                    return new GenericStatement();
                }
            }
        } else if (isLocal()) {
            List<String> nameList = isNameList();

            if (nameList != null) {
                variables = new ArrayList<Variable>();

                for (String name : nameList) {
                    Variable variable = new Variable(name, scope);
                    variables.add(variable);

                    try {
                        program.addLocalVariable(parentScope, scope, variable);

                    } catch (SemanthicException ex) {
                        printError(ex.getErrorNumber());
                        return null;
                    }
                }

                if (isAssign() != null) {
                    List<List<Node>> expressions = isExpList(scope);
                    if (expressions == null || expressions.size() != variables.size()) return null;

                    return new GroupAssignStatement(variables, expressions);
                }

                return new GenericStatement();
            } else {
                printError(519);
            }
        }

        return null;
    }

    //laststat ::= return [explist] | break
    private boolean isLastStat(int scope) {
        if (isReturn()) {
            List<List<Node>> expressions = isExpList(scope);
            if (expressions != null) {
                this.hasReturnValue = true;
                return true;
            }
            return true;

        } else if(isBreak()) {
            return true;
        }

        return false;
    }

    //funcname ::= Name {`.´ Name} [`:´ Name]
    private boolean isFuncName(){
        if(isName()){
            getNextToken();
            return true;
        }
        return false;
    }

    //var ::=  Name | prefixexp `[´ exp `]´ | prefixexp `.´ Name
    private Variable isVar(int scope){
        if(isName()) {
            variableConstruct = new ArrayDeque<>();
            String variableName = currentToken.lexeme;
            variableConstruct.add(variableName);

            getNextToken();
            if(isVarTail(scope)) {
                Variable variable = new Variable(variableConstruct, scope);
                variableConstruct = null;
                return variable;
            }
        }
        else if(isPrefixExp(scope).size() > 0) {
            variableConstruct = new ArrayDeque<>();
            if(isLeftBracket()) {
                if(isExp(scope) != null){
                    if(isRightBracket()){
                        if(isVarTail(scope)) {
                            Variable variable = new Variable(variableConstruct,scope);
                            variableConstruct = null;
                            return variable;
                        }
                    } else {
                        printError(504);
                    }
                }
            } else if(isDot()) {
                if(isName()){
                    variableConstruct.add(currentToken.lexeme);
                    getNextToken();
                    if(isVarTail(scope)) {
                        Variable variable = new Variable(variableConstruct, scope);
                        variableConstruct = null;
                        return variable;
                    }
                }else {
                    printError(511);
                }
            }else{
                printError(509);
            }
        }
        else if(isLeftParenthesis() != null) {
            if (isExp(scope) != null) {
                if (isRightParenthesis() != null) {
                    if (isArgs(scope)) {
                        if (isFunctionCallTail(scope)) {
                            return new Variable(TypeEnum.VOID);
                        }
                    }
                    else if (isColon()) {
                        if (isName()) {
                            getNextToken();
                            if (isArgs(scope)) {
                                if (isFunctionCallTail(scope)) {
                                    return new Variable(TypeEnum.VOID);
                                }
                            }
                        } else {
                            printError(511);
                        }
                    } else {
                        printError(508);
                    }
                } else {
                    printError(507);
                }
            }
        }

        return null;
    }

    private boolean isVarTail(int scope) {
        if (isLeftBracket()) {
            if (isExp(scope) != null) {
                if (isRightBracket()) {
                    if (isVarTail(scope)) {
                        return true;
                    }
                } else {
                    printError(504);
                    return false;
                }
            }
        } else if (isDot()) {
            if (isName()) {
                variableConstruct.add(currentToken.lexeme);
                getNextToken();
                if (isVarTail(scope)) {
                    return true;
                }
            } else {
                printError(511);
            }
        } else if (isArgs(scope)) {
            if (isFunctionCallTail(scope)) {
                if (isVarTail(scope)) {
                    return true;
                }
            }
        } else if (isColon()) {
            if (isName()) {
                getNextToken();
                if (isArgs(scope)) {
                    if (isFunctionCallTail(scope)) {
                        if (isVarTail(scope)) {
                            return true;
                        }
                    }
                }
            } else {
                printError(511);
            }
        } else {
            return true;
        }

        return false;
    }

    //explist ::= {exp `,´} exp
    private List<List<Node>> isExpList(int scope) {
        List<List<Node>> expressions = new ArrayList<>();
        List<Node> expression = isExp(scope);

        if (expression.size() > 0){
            expressions.add(expression);

            while(isComma()){
                expression = isExp(scope);
                if (expression.size() > 0) {
                    expressions.add(expression);
                } else {
                    return null;
                }
            }
            return expressions;
        }

        return null;
    }

    /*exp ::=  nil | false | true | Number | String | function |
		 prefixexp | tableconstructor | exp binop exp | unop exp */
    private List<Node> isExp(int scope) {
        return isExp(new ArrayList<>(), scope);
    }

    private List<Node> isExp(List<Node> currentExpression, int scope) {
        Token token;

        token= isNil();
        if (token != null) {
            currentExpression.add(new ValueNode(token));
            return isExpTail(currentExpression, scope);
        }

        token = isFalse();
        if (token != null) {
            currentExpression.add(new ValueNode(token));
            return isExpTail(currentExpression, scope);
        }

        token = isTrue();
        if (token != null) {
            currentExpression.add(new ValueNode(token));
            return isExpTail(currentExpression, scope);
        }

        token = isNumber();
        if (token != null) {
            currentExpression.add(new ValueNode(token));
            return isExpTail(currentExpression, scope);
        }

        token = isString();
        if (token != null) {
            currentExpression.add(new ValueNode(token));
            return isExpTail(currentExpression, scope);
        }

        token = isUnop();
        if (token != null) {
            currentExpression.add(new ValueNode(token));
            return isExpTail(currentExpression, scope);
        }

        List<Node> tokens = isPrefixExp(currentExpression, scope);
        if (currentExpression.size() < tokens.size()) {
            return isExpTail(tokens, scope);
        }

        Variable variable = isVar(scope);
        if (variable != null) {
            if (isArgs(scope)) {
                if (isFunctionCallTail(scope)) {

                }
            } else if (isColon()) {
                if (isName()) {
                    getNextToken();
                    if (isFunctionCallTail(scope)) {

                    }
                } else {
                    printError(511);
                }
            } else {
                currentExpression.add(variable);
            }

            return isExpTail(currentExpression, scope);

        } else if (isFunctionCall(scope)) {
            return isExpTail(currentExpression, scope);
        }

        return currentExpression;
    }

    private List<Node> isExpTail(List<Node> currentExpression, int scope) {
        Token binop = isBinop();
        if (binop != null) {
            OperatorFactory operatorFactory = new OperatorFactory();

            currentExpression.add(operatorFactory.getOperator(binop));
            int originalSize = currentExpression.size();
            currentExpression = isExp(currentExpression, scope);

            if (originalSize < currentExpression.size()) {
                return isExpTail(currentExpression, scope);
            }
        }

        return currentExpression;
    }

    //varlist ::= var {`,´ var}
    private List<Variable> isVarList(int scope){
        ArrayList<Variable> variables = new ArrayList<>();

        Variable variable = isVar(scope);
        if(variable != null){
                variables.add(variable);
            while(isComma()){
                variable = isVar(scope);
                variables.add(variable);
            }
        }

        return variables;
    }

    //prefixexp ::= var | functioncall | `(´ exp `)´
    private List<Node> isPrefixExp(List<Node> currentExpression, int scope) {
        if (isLeftParenthesis() != null) {
            int originalSize = currentExpression.size();
            currentExpression = isExp(currentExpression, scope);
            if (originalSize < currentExpression.size()) {
                if (isRightParenthesis() != null) {
                    return currentExpression;
                } else {
                    printError(507);
                }
            }
        }

        return currentExpression;
    }

    private List<Node> isPrefixExp(int scope) {
        return isPrefixExp(new ArrayList<>(), scope);
    }

    //functioncall ::=  prefixexp args | prefixexp `:´ Name args
    private boolean isFunctionCall(int scope){
        if (isVar(scope) != null) {
            if (isArgs(scope)) {
                if (isFunctionCallTail(scope)) {
                    return true;
                }
            } else if (isColon()) {
                if (isName()) {
                    getNextToken();
                    if (isArgs(scope)) {
                        if (isFunctionCallTail(scope)) {
                            return true;
                        }
                    }
                }
            } else {
                printError(508);
            }
        } else if (isLeftParenthesis() != null) {
            if (isExp(scope) != null) {
                if (isRightParenthesis() != null) {
                    if (isArgs(scope)) {
                        if (isFunctionCallTail(scope)) {
                            return true;
                        }
                    } else if (isColon()) {
                        if (isName()) {
                            getNextToken();
                            if (isArgs(scope)) {
                                if (isFunctionCallTail(scope)) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        printError(508);
                    }
                } else {
                    printError(507);
                }
            }
        }

        return false;
    }

    private boolean isFunctionCallTail(int scope) {
        if (isArgs(scope)) {
            if (isFunctionCallTail(scope)) {
                return true;
            }
        } else if (isColon()) {
            if (isName()) {
                getNextToken();
                if (isArgs(scope)) {
                    if (isFunctionCallTail(scope)) {
                        return true;
                    }
                }
            } else {
                printError(511);
            }
        } else {
            return true;
        }

        return false;
    }

    //args ::=  `(´ [explist] `)´ | tableconstructor | String
    private boolean isArgs(int scope) {
        if (isLeftParenthesis() != null) {
            List<List<Node>> expressions = isExpList(scope);

            if (expressions != null) {
                if (isRightParenthesis() != null) {
                    return true;
                } else {
                    printError(507);
                }
            } else if (isRightParenthesis() != null) {
                return true;
            } else {
                printError(507);
            }
        } else if (isTableConstructor(scope)) {
            return true;
        } else if (isString() != null) {
            return true;
        }

        return false;
    }

    private boolean isFunctionKeyword() {
        if (currentToken.id != 208) return false;
        getNextToken();
        return true;
    }

    //funcbody ::= `(´ [parlist] `)´ block end
    private boolean isFuncBody(int scope, int parentScope) throws SemanthicException {
        function = new Function();
        Token functionName = peekPreviousToken();
        function.setName(functionName.lexeme);

        try {
            program.addFunction(function);

        } catch (SemanthicException ex) {
            printError(ex.getErrorNumber());
            return false;
        }

        if (isLeftParenthesis() != null) {
            List<Parameter> parameters = isParList();

            if (parameters != null) {
                function.addParameters(parameters);
                program.updateFunction(function);

                if (isRightParenthesis() != null) {
                    List<Statement> statements = isBlock(scope, scope);

                    if (statements != null) {
                        function.setHasReturnValue(this.hasReturnValue);
                        function.setStatements(statements);
                        program.updateFunction(function);
                        hasReturnValue = false;

                        if (isEnd()) {
                            return true;
                        }

                    } else {
                        printError(513);
                    }
                }
            } else if (isRightParenthesis() != null) {
                List<Statement> statements = isBlock(scope, scope);

                if (statements != null) {
                    function.setHasReturnValue(this.hasReturnValue);
                    function.setStatements(statements);
                    program.updateFunction(function);
                    hasReturnValue = false;

                    if (isEnd()) {
                        function = null;
                        return true;

                    } else {
                        printError(513);
                    }
                }
            }
        }

        printError(520);
        return false;
    }


    //parlist ::= namelist [`,´ `...´] | `...´
    private List<Parameter> isParList(){
        List<String> nameList = isNameList();
        if(nameList != null) {
            ArrayList<Parameter> parameters = new ArrayList<>();
            for (String name : nameList) {
                Parameter parameter = new Parameter(name);
                parameters.add(parameter);
            }

            return parameters;
        }

        return null;
    }

    //namelist ::= Name {`,´ Name}
    private List<String> isNameList(){
        List<String> nameList = new ArrayList<>();

        if (isName()) {
            nameList.add(currentToken.lexeme);
            getNextToken();

            while (isComma()) {
                if (!isName()) {
                    printError(511);
                    return null;
                }

                nameList.add(currentToken.lexeme);
                getNextToken();
            }

            return nameList;
        }

        return null;
    }


    //tableconstructor ::= `{´ [fieldlist] `}´
    private boolean isTableConstructor(int scope){
        if(isLeftCurlyBracket()){
            if(isFieldList(scope)){
                if(isRightCurlyBracket()){
                    return true;

                } else {
                    printError(505);
                }

            } else if(isRightCurlyBracket()) {
                return true;
            } else {
                printError(505);
            }
        }

        return false;
    }

    //fieldlist ::= field {fieldsep field} [fieldsep]
    private boolean isFieldList(int scope){
        if (isField(scope)) {
            while (isFieldSep()) {
                if (!isField(scope)) break;
            }
            return true;
        }

        return false;
    }

    private boolean isField(int scope){
        if (isLeftBracket()){
            if (isExp(scope) != null){
                if (isRightBracket()){
                    if (isAssign() != null){
                        if(isExp(scope) != null) {
                            return true;
                        }

                    } else {
                        printError(503);
                    }
                } else {
                   printError(504);
                }
            }
        } else if (isName()) {
            getNextToken();
            if (isAssign() != null) {
                if (isExp(scope) != null) {
                    return true;
                }
            } else {
                printError(503);
            }
        } else if (isExp(scope) != null) {
            return true;
        }

        return false;
    }

    private boolean isName() {
        return currentToken.id == 100;
    }

    private boolean isDot() {
        if (currentToken.id != 125) return false;
        getNextToken();
        return true;
    }

    private boolean isEnd() {
        if (currentToken.id != 205) return false;
        getNextToken();
        return true;
    }

    private boolean isBreak() {
        if (currentToken.id != 201) return false;
        getNextToken();
        return true;
    }

    private boolean isReturn() {
        if (currentToken.id != 216) return false;
        getNextToken();
        return true;
    }

    private Token isAssign() {
        if (currentToken.id != 115) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private boolean isRightBracket() {
        if (currentToken.id != 121) return false;
        getNextToken();
        return true;
    }

    private Token isString() {
        if (currentToken.id != 128) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private boolean isColon() {
        if (currentToken.id != 123) return false;
        getNextToken();
        return true;
    }

    private Token isNumber() {
        if (currentToken.id != 101) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isTrue() {
        if (currentToken.id != 218) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isFalse() {
        if (currentToken.id != 206) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isNil() {
        if (currentToken.id != 212) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private boolean isLocal() {
        if (currentToken.id != 211) return false;
        getNextToken();
        return true;
    }

    private boolean isFor() {
        if (currentToken.id != 207) return false;
        getNextToken();
        return true;
    }

    private boolean isElse() {
        if (currentToken.id != 203) return false;
        getNextToken();
        return true;
    }

    private boolean isElseIf() {
        if (currentToken.id != 204) return false;
        getNextToken();
        return true;
    }

    private boolean isThen() {
        if (currentToken.id != 217) return false;
        getNextToken();
        return true;
    }

    private boolean isIf() {
        if (currentToken.id != 209) return false;
        getNextToken();
        return true;
    }

    private boolean isDo() {
        if (currentToken.id != 202) return false;
        getNextToken();
        return true;
    }

    private Token isRightParenthesis(){
        if (currentToken.id != 117) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isLeftParenthesis(){
        if (currentToken.id != 116) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private boolean isLeftCurlyBracket() {
        if (currentToken.id != 118) return false;
        getNextToken();
        return true;
    }

    private boolean isRightCurlyBracket() {
        if (currentToken.id != 119) return false;
        getNextToken();
        return true;
    }

    private boolean isLeftBracket() {
        if (currentToken.id != 120) return false;
        getNextToken();
        return true;
    }

    private boolean isSemicolon() {
        if (currentToken.id != 122) return false;
        getNextToken();
        return true;
    }

    private boolean isComma() {
        if (currentToken.id != 124) return false;
        getNextToken();
        return true;
    }

    private Token isHash() {
        if (currentToken.id != 108) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isNot() {
        if (currentToken.id != 213) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isOr() {
        if (currentToken.id != 214) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isAnd() {
        if (currentToken.id != 200) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isDifferent() {
        if (currentToken.id != 110) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isEqual() {
        if (currentToken.id != 109) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isGreaterOrEqualThan() {
        if (currentToken.id != 112) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isGreaterThan() {
        if (currentToken.id != 114) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isLessOrEqualThan() {
        if (currentToken.id != 111) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isLessThan() {
        if (currentToken.id != 113) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isDoubleDot() {
        if (currentToken.id != 126) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isPercent() {
        if (currentToken.id != 106) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isCircumflexAccent() {
        if (currentToken.id != 107) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isSlash() {
        if (currentToken.id != 105) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isAsterisk() {
        if (currentToken.id != 104) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isMinus() {
        if (currentToken.id != 103) return null;
        getNextToken();
        return peekPreviousToken();
    }

    private Token isPlus() {
        if (currentToken.id != 102) return null;
        getNextToken();
        return peekPreviousToken();

    }

    private void getNextToken() {
        try {
            currentToken = iterator.next();
        }
        catch (NoSuchElementException exception) {
            currentToken = null;
        }
    }

    private Token peekPreviousToken() {
        iterator.previous();
        Token previous = iterator.previous();
        iterator.next();
        iterator.next();
        return previous;
    }

}

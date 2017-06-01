package Compiler;

import Expressions.ASTNode;
import Expressions.ShuntingYardParser;
import Statements.*;

import javax.swing.*;
import java.beans.Expression;
import java.util.*;

/**
 * Created by beatrizzamorano on 1/1/17.
 */
public class SyntacticalAnalysis {
    private List<Token> tokens;
    private ListIterator<Token> iterator;
    private Token currentToken;
    private String errorStack = "";
    private Program program;
    private boolean hasReturnValue;
    private Function function;
    private Queue<String> variableConstruct;
    private ShuntingYardParser expressionParser;

    public SyntacticalAnalysis(List<Token> tokens) {
        this.tokens = tokens;
        this.iterator = tokens.listIterator();
        program = new Program();
        expressionParser = new ShuntingYardParser();

        getNextToken();
        List<Statement> block = isBlock();
        if(block != null && errorStack.isEmpty()) {
            for (Statement statement : block) {
                this.program.addStatement(statement);
            }

            JOptionPane.showMessageDialog(null, "Sintaxis correcta");
        } else {
            JOptionPane.showMessageDialog(null, errorStack);
        }
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

    private List<Statement> isBlock() {
        return isChunk();
    }

    //chunk ::= {stat [`;´]} [laststat [`;´]]

    private List<Statement> isChunk(){
        List<Statement> statements = new ArrayList<>();

        try {

            Statement statement = isStat();
            while (statement != null) {
                statements.add(statement);
                isSemicolon();

                statement = isStat();
            }

            if (isLastStat()) {
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
		 while exp do block end |
		 if exp then block {elseif exp then block} [else block] end |
		 for Name `=´ exp `,´ exp [`,´ exp] do block end |
		 function funcname funcbody |
		 local namelist [`=´ explist] */


    private Statement isStat(){
        List<Variable> variables = isVarList();

        if (variables != null && variables.size() > 0) {
            for (Variable variable : variables) {
                try {
                    program.addGlobalVariable(variable);

                } catch (SemanthicException ex) {
                    printError(ex.getErrorNumber());
                }
            }

            if (variables.size() > 1) {
                if (isAssign()) {
                    List<List<Token>> expressions = isExpList();
                    if (expressions != null) {
                        if (variables.size() != expressions.size()) return null;

                        return new GroupAssignStatement(variables, expressions);
                    }
                } else {
                    printError(503);
                }
            } else if (variables.size() == 1) {
//                if (isArgs()) {
//                    if (isFunctionCallTail()) {
//                        return true;
//                    }
//                }
//                else if (isColon()) {
//                    if (isName()) {
//                        getNextToken();
//                        if (isArgs()) {
//                            if (isFunctionCallTail()) {
//                                return true;
//                            }
//                        }
//                    }
//                }
                if (isAssign()) {
                    List<List<Token>> expressions = isExpList();
                    if (expressions != null) {
                        return new AssignStatement(variables.get(0), expressions.get(0));
                    }
                }

                return new GenericStatement();
            }
        } else if (isFunctionCall()) {
            return new GenericStatement();
//            return true;
        }
//        else if (isWhile()) {
//            if (isExp() != null) {
//                if (isDo()) {
//                    if (isBlock()) {
//                        if (isEnd()) {
//                            return true;
//                        } else {
//                            printError(513);
//                        }
//                    }
//                } else {
//                    printError(515);
//                }
//            }
//        }
        else if (isIf()) {
            List<Token> expression = isExp();
            if (expression != null) {
                IfStatement ifStatement = new IfStatement(expression);

                if (isThen()) {
                    List<Statement> firstConditionalStatements = isBlock();
                    if (firstConditionalStatements != null) {
                        ifStatement.setStatements(firstConditionalStatements);

                        while (isElseIf()) {
                            List<Token> elseIfExpression = isExp();

                            if (elseIfExpression != null) {
                                if (isThen()) {
                                    List<Statement> block = isBlock();
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
                            List<Statement> block = isBlock();
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
//        else if (isFor()) {
//            int nameCount = isNameWithCount();
//            if (nameCount == 1) {
//                if (isAssign()) {
//                    if (isExp() != null) {
//                        if (isComma()) {
//                            if (isExp() != null) {
//                                if (isComma() && isExp() != null) {
//                                    if (isDo()) {
//                                        if (isBlock()) {
//                                            if (isEnd()) {
//                                                return true;
//                                            } else {
//                                                printError(513);
//                                            }
//                                        }
//                                    } else {
//                                        printError(515);
//                                    }
//                                } else if(isDo()) {
//                                    if (isBlock()) {
//                                        if (isEnd()) {
//                                            return true;
//                                        } else {
//                                            printError(513);
//                                        }
//                                    }
//                                } else {
//                                    printError(515);
//                                }
//                            }
//                        } else {
//                            printError(506);
//                            return null;
//                        }
//                    }
//                } else {
//                    printError(518);
//                }
//            } else {
//                printError(511);
//                return null;
//            }
//
//        }
        else if (isFunctionKeyword()) {
            if (isFuncName()) {
                if (isFuncBody()) {
                    return new GenericStatement();
                }
            }
        } else if (isLocal()) {
            List<String> nameList = isNameList();

            if (nameList != null) {
                variables = new ArrayList<Variable>();

                for (String name : nameList) {
                    Variable variable = new Variable(name);
                    variables.add(variable);

                    try {
                        program.addLocalVariable(function.getName(), variable);

                    } catch (SemanthicException ex) {
                        printError(ex.getErrorNumber());
                        return null;
                    }
                }

                if (isAssign()) {
                    List<List<Token>> expressions = isExpList();
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

    private boolean isLastStat() {
        if (isReturn()) {
            List<List<Token>> expressions = isExpList();
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

    private Variable isVar(){
        if(isName()) {
            variableConstruct = new ArrayDeque<>();
            String variableName = currentToken.lexeme;
            variableConstruct.add(variableName);

            getNextToken();
            if(isVarTail()) {
                Variable variable = new Variable(variableConstruct);
                variableConstruct = null;
                return variable;
            }
        }
        else if(isPrefixExp().size() > 0) {
            variableConstruct = new ArrayDeque<>();
            if(isLeftBracket()) {
                if(isExp() != null){
                    if(isRightBracket()){
                        if(isVarTail()) {
                            Variable variable = new Variable(variableConstruct);
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
                    if(isVarTail()) {
                        Variable variable = new Variable(variableConstruct);
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
            if (isExp() != null) {
                if (isRightParenthesis() != null) {
                    if (isArgs()) {
                        if (isFunctionCallTail()) {
                            return new Variable(TypeEnum.VOID);
                        }
                    }
                    else if (isColon()) {
                        if (isName()) {
                            getNextToken();
                            if (isArgs()) {
                                if (isFunctionCallTail()) {
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

    private boolean isVarTail() {
        if (isLeftBracket()) {
            if (isExp() != null) {
                if (isRightBracket()) {
                    if (isVarTail()) {
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
                if (isVarTail()) {
                    return true;
                }
            } else {
                printError(511);
            }
        } else if (isArgs()) {
            if (isFunctionCallTail()) {
                if (isVarTail()) {
                    return true;
                }
            }
        } else if (isColon()) {
            if (isName()) {
                getNextToken();
                if (isArgs()) {
                    if (isFunctionCallTail()) {
                        if (isVarTail()) {
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

    private List<List<Token>> isExpList() {
        List<List<Token>> expressions = new ArrayList<>();
        List<Token> expression = isExp();

        if (expression.size() > 0){
            expressions.add(expression);

            while(isComma()){
                expression = isExp();
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
		 prefixexp | tableconstructor | exp binop exp | unop exp
*/

    private List<Token> isExp() {
        return isExp(new ArrayList<>());
    }


    private List<Token> isExp(List<Token> currentExpression) {
        Token token;

        token= isNil();
        if (token != null) {
            currentExpression.add(token);
            return isExpTail(currentExpression);
        }

        token = isFalse();
        if (token != null) {
            currentExpression.add(token);
            return isExpTail(currentExpression);
        }

        token = isTrue();
        if (token != null) {
            currentExpression.add(token);
            return isExpTail(currentExpression);
        }

        token = isNumber();
        if (token != null) {
            currentExpression.add(token);
            return isExpTail(currentExpression);
        }

        token = isString();
        if (token != null) {
            currentExpression.add(token);
            return isExpTail(currentExpression);
        }

        token = isUnop();
        if (token != null) {
            currentExpression.add(token);
            return isExpTail(currentExpression);
        }

        List<Token> tokens = isPrefixExp(currentExpression);
        if (currentExpression.size() < tokens.size()) {
            return isExpTail(tokens);
        }

//        else if(isTableConstructor()){
//            if (isExpTail()) {
//
//            }
//        }
         else if (isVar() != null) {
            if (isArgs()) {
                if (isFunctionCallTail()) {

                }
            } else if (isColon()) {
                if (isName()) {
                    getNextToken();
                    if (isFunctionCallTail()) {

                    }
                } else {
                    printError(511);
                }
            }

            return isExpTail(currentExpression);

        } else if (isFunctionCall()) {
            return isExpTail(currentExpression);
        }

        return currentExpression;
    }

    private List<Token> isExpTail(List<Token> currentExpression) {
        Token binop = isBinop();
        if (binop != null) {
            currentExpression.add(binop);
            int originalSize = currentExpression.size();
            currentExpression = isExp(currentExpression);

            if (originalSize < currentExpression.size()) {
                return isExpTail(currentExpression);
            }
        }

        return currentExpression;
    }


    //varlist ::= var {`,´ var}

    private List<Variable> isVarList(){
        ArrayList<Variable> variables = new ArrayList<>();

        Variable variable = isVar();
        if(variable != null){
            if (variable.getType() != TypeEnum.VOID) {
                variables.add(variable);
            }
            while(isComma()){
                variable = isVar();
                if(variable != null && variable.getType() != TypeEnum.VOID){
                    variables.add(variable);
                }
            }
        }

        return variables;
    }

    //prefixexp ::= var | functioncall | `(´ exp `)´

    private List<Token> isPrefixExp(List<Token> currentExpression) {
        if (isLeftParenthesis() != null) {
            int originalSize = currentExpression.size();
            currentExpression = isExp(currentExpression);
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

    private List<Token> isPrefixExp() {
        return isPrefixExp(new ArrayList<>());
    }


    //functioncall ::=  prefixexp args | prefixexp `:´ Name args

    private boolean isFunctionCall(){
        if (isVar() != null) {
            if (isArgs()) {
                if (isFunctionCallTail()) {
                    return true;
                }
            } else if (isColon()) {
                if (isName()) {
                    getNextToken();
                    if (isArgs()) {
                        if (isFunctionCallTail()) {
                            return true;
                        }
                    }
                }
            } else {
                printError(508);
            }
        } else if (isLeftParenthesis() != null) {
            if (isExp() != null) {
                if (isRightParenthesis() != null) {
                    if (isArgs()) {
                        if (isFunctionCallTail()) {
                            return true;
                        }
                    } else if (isColon()) {
                        if (isName()) {
                            getNextToken();
                            if (isArgs()) {
                                if (isFunctionCallTail()) {
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

    private boolean isFunctionCallTail() {
        if (isArgs()) {
            if (isFunctionCallTail()) {
                return true;
            }
        } else if (isColon()) {
            if (isName()) {
                getNextToken();
                if (isArgs()) {
                    if (isFunctionCallTail()) {
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

    private boolean isArgs() {
        if (isLeftParenthesis() != null) {
            List<List<Token>> expressions = isExpList();

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
        } else if (isTableConstructor()) {
            return true;
        } else if (isString() != null) {
            return true;
        }

        return false;
    }

    //function ::= function funcbody

    private boolean isFunction(){
        if(isFunctionKeyword()){
            if(isFuncBody()) {
                return true;
            }
        }

        return false;
    }

    private boolean isFunctionKeyword() {
        if (currentToken.id != 208) return false;
        getNextToken();
        return true;
    }

    //funcbody ::= `(´ [parlist] `)´ block end

    private boolean isFuncBody() {
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
                    List<Statement> statements = isBlock();

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
                List<Statement> statements = isBlock();

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
        ArrayList nameList = new ArrayList();
        if(isName()){
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

    private boolean isTableConstructor(){
        if(isLeftCurlyBracket()){
            if(isFieldList()){
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

    private boolean isFieldList(){
        if (isField()) {
            while (isFieldSep()) {
                if (!isField()) break;
            }
            return true;
        }

        return false;
    }

    private boolean isField(){
        if (isLeftBracket()){
            if (isExp() != null){
                if (isRightBracket()){
                    if (isAssign()){
                        if(isExp() != null) {
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
            if (isAssign()) {
                if (isExp() != null) {
                    return true;
                }
            } else {
                printError(503);
            }
        } else if (isExp() != null) {
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


    private boolean isAssign() {
        if (currentToken.id != 115) return false;
        getNextToken();
        return true;
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

    private boolean isUntil() {
        if (currentToken.id != 219) return false;
        getNextToken();
        return true;
    }

    private boolean isRepeat() {
        if (currentToken.id != 215) return false;
        getNextToken();
        return true;
    }

    private boolean isWhile() {
        if (currentToken.id != 220) return false;
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

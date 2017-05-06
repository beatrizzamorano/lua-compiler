import javax.swing.*;
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

    public SyntacticalAnalysis(List<Token> tokens) {
        this.tokens = tokens;
        this.iterator = tokens.listIterator();
        program = new Program();

        getNextToken();
        if(isBlock() && errorStack.isEmpty()) {
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
            case 519 : return " 'Function' expected.";
            case 520 : return " '(' expected.";
            case 521 : return "Function already declared";
            case 522 : return "Tried to declare a local variable with the name of an existing global variable.";
            case 523 : return "Tried to declare a local variable with the name of an existing function.";
            case 524 : return "Tried to declare a function with the name of an existing global variable.";
            case 525 : return "Tried to declare a global variable with the name of an existing function.";

            default : return "";
        }

    }

    private boolean isBinop() {
        if (isPlus() ||
                isMinus() ||
                isAsterisk() ||
                isSlash() ||
                isCircumflexAccent() ||
                isPercent() ||
                isDoubleDot() ||
                isLessThan() ||
                isLessOrEqualThan() ||
                isGreaterThan() ||
                isGreaterOrEqualThan() ||
                isEqual() ||
                isDifferent() ||
                isAnd() ||
                isOr()) return true;
        return false;
    }

    private boolean isUnop(){
        if (isMinus() ||
                isNot() ||
                isHash()) return true;
        return false;
    }

    private boolean isFieldSep(){
        if (isComma() ||
                isSemicolon()) return true;
        return false;
    }

    //block ::= chunk

    private boolean isBlock(){
        if(isChunk()){
            return true;
        }

        return false;
    }

    //chunk ::= {stat [`;´]} [laststat [`;´]]

    private boolean isChunk(){
        try {
            while(isStat()) {
                isSemicolon();
            }
            if(isLastStat()) {
                isSemicolon();
            }
            return true;
        } catch (NullPointerException ex) {
            return true;
        }
    }

    /*stat ::=
         varlist `=´ explist |
		 functioncall |
		 do block end |
		 while exp do block end |
		 repeat block until exp |
		 if exp then block {elseif exp then block} [else block] end |
		 for Name `=´ exp `,´ exp [`,´ exp] do block end |
		 for namelist in explist do block end |
		 function funcname funcbody |
		 local namelist [`=´ explist] */


    private boolean isStat(){
        List<Variable> variables = isVarList();

        if(variables != null && variables.size() > 0){
            for (Variable variable : variables) {
                try {
                    program.addGlobalVariable(variable);

                } catch (SemanthicException ex) {
                    printError(ex.getErrorNumber());
                }
            }

            if (variables.size() > 1) {
                if(isAssign()){
                    if(isExpList()){
                        return true;
                    }
                }else {
                    printError(503);
                }
            } else if (variables.size() == 1){
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
                } else if(isAssign()){
                    if(isExpList()){
                        return true;
                    }
                }
                return true;
            }
        } else if(isDo()){
            if(isBlock()){
                if(isEnd()){
                    return true;
                } else {
                    printError(513);
                }
            }
        }else if(isFunctionCall()){
            return true;
        }else if(isWhile()){
            if(isExp()){
                if(isDo()){
                    if(isBlock()){
                        if(isEnd()){
                            return true;
                        } else{
                            printError(513);
                        }
                    }
                } else{
                    printError(515);
                }
            }
        }else if(isRepeat()){
            if(isBlock()){
                if(isUntil()){
                    if(isExp()){
                        return true;
                    }
                } else{
                    printError(516);
                }
            }
        }else if(isIf()){
            if(isExp()){
                if(isThen()){
                    if(isBlock()){
                        while(isElseIf()){
                            if(isExp()){
                                if(isThen()){
                                    if (!isBlock()) {
                                        return false;
                                    }
                                } else {
                                    printError(517);
                                }
                            }

                        }
                        if(isElse()){
                            if(!isBlock()){
                                return false;
                            }
                        }
                       if(isEnd()){
                           return true;
                       } else{
                           printError(513);
                           return false;
                       }
                    }
                } else {
                    printError(517);
                }
            }
        } else if (isFor()) {
            int nameCount = isNameWithCount();
            if(nameCount == 1){
                if(isAssign()){
                    if(isExp()){
                        if(isComma()){
                            if(isExp()){
                                if(isComma() && isExp()){
                                    if(isDo()){
                                        if(isBlock()){
                                            if(isEnd()){
                                                return true;
                                            } else {
                                                printError(513);
                                            }
                                        }
                                    } else {
                                        printError(515);
                                    }
                                }else if(isDo()){
                                    if(isBlock()){
                                        if(isEnd()){
                                            return true;
                                        } else {
                                            printError(513);
                                        }
                                    }
                                } else {
                                    printError(515);
                                }
                            }
                        } else {
                            printError(506);
                            return false;
                        }
                    }
                } else {
                    printError(518);
                }
            } else {
                printError(511);
                return false;
            }

        } else if(isFunctionKeyword()){
            if(isFuncName()){
                if(isFuncBody()){
                    return true;
                }
            }
        }else if(isLocal()){
            List<String> nameList = isNameList();

            if(nameList != null){
                for (String name : nameList) {
                    Variable variable = new Variable(name);
                    try {
                        program.addLocalVariable(function.getName(), variable);
                    } catch (SemanthicException ex) {
                        printError(ex.getErrorNumber());
                        return false;
                    }
                }

                if(isAssign()){
                    if (!isExpList()) {
                        return false;
                    }
                }
                return true;
            } else {
                printError(519);
            }
        }

        return false;
    }

    //laststat ::= return [explist] | break

    private boolean isLastStat(){
        if(isReturn()){
            if(isExpList()){
                this.hasReturnValue = true;
                return true;
            }
            return true;

        }else if(isBreak()){
            return true;
        }

        return false;
    }

    private int isNameWithCount() {
        int count = 0;
        if (isName()) {
            getNextToken();
            count++;
            while(isComma()) {
                count++;
                if (!isName()) {
                    printError(511);
                    return -1;
                }
                getNextToken();
            }
        }

        return count;
    }



    //funcname ::= Name {`.´ Name} [`:´ Name]
    private boolean isFuncName(){
        if(isName()){
            Token token = peekPreviousToken();
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
        else if(isPrefixExp()) {
            variableConstruct = new ArrayDeque<>();
            if(isLeftBracket()) {
                if(isExp()){
                    if(isRightBracket()){
                        if(isVarTail()) {
                            Variable variable = new Variable(variableConstruct);
                            variableConstruct = null;
                            return variable;
                        }
                    }else {
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
        else if(isLeftParenthesis()) {
            if (isExp()) {
                if (isRightParenthesis()) {
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
            if (isExp()) {
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

    private boolean isExpList(){
        if(isExp()){
            while(isComma()){
                if (!isExp()) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /*exp ::=  nil | false | true | Number | String | `...´ | function |
		 prefixexp | tableconstructor | exp binop exp | unop exp
*/


    private boolean isExp(){
        if(isNil()){
            if (isExpTail()) {
                return true;
            }
        }else if(isFalse()){
            if (isExpTail()) {
                return true;
            }
        }else if(isTrue()){
            if (isExpTail()) {
                return true;
            }
        }else if(isNumber()){
            if (isExpTail()) {
                return true;
            }
        }else if(isString()){
            if (isExpTail()) {
                return true;
            }
        }else if(isTripleDot()){
            if (isExpTail()) {
                return true;
            }
        }else if(isFunction()){
            if (isExpTail()) {
                return true;
            }
        }else if(isPrefixExp()){
            if (isExpTail()) {
                return true;
            }
        }else if(isTableConstructor()){
            if (isExpTail()) {
                return true;
            }
        }
        else if(isUnop()){
            if(isExp()){
                if (isExpTail()) {
                    return true;
                }
            }
        } else if (isVar() != null) {
            if (isArgs()) {
                if (isFunctionCallTail()) {
                    return true;
                }
            } else if (isColon()) {
                if (isName()) {
                    getNextToken();
                    if (isFunctionCallTail()) {
                        return true;
                    }
                } else {
                    printError(511);
                }
            }
            else if (isExpTail()) {
                return true;
            }
        } else if (isFunctionCall()) {
            if (isExpTail()) {
                return true;
            }
        }

        return false;

    }

    private boolean isExpTail() {
        if (isBinop()) {
            if (isExp()) {
                if (isExpTail()) {
                    return true;
                }
            }
        }

        return true;
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

    private boolean isPrefixExp() {
        if(isLeftParenthesis()){
            if(isExp()){
                if(isRightParenthesis()){
                    return true;
                }else {
                    printError(507);
                }
            }
        }

        return false;
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
        } else if (isLeftParenthesis()) {
            if (isExp()) {
                if (isRightParenthesis()) {
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

    private boolean isArgs(){
        if(isLeftParenthesis()){
            if(isExpList()){
                if(isRightParenthesis()){
                    return true;
                } else {
                    printError(507);
                }
            } else if(isRightParenthesis()) {
                return true;
            } else {
                printError(507);
            }
        }else if(isTableConstructor()){
            return true;
        }else if(isString()){
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

    private boolean isFuncBody(){
        function = new Function();
        Token functionName = peekPreviousToken();
        function.setName(functionName.lexeme);
        try {
            program.addFunction(function);
        } catch (SemanthicException ex) {
            printError(ex.getErrorNumber());
            return false;
        }

        if(isLeftParenthesis()){
            List<Parameter> parameters = isParList();
            if(parameters != null){
                function.addParameters(parameters);
                program.updateFunction(function);
                if(isRightParenthesis()) {
                    if(isBlock()){
                        function.setHasReturnValue(this.hasReturnValue);
                        program.updateFunction(function);
                        hasReturnValue = false;

                        if(isEnd()){
                            return true;
                        }
                    } else{
                        printError(513);
                    }
                }
            } else if(isRightParenthesis()){
                if(isBlock()){
                    function.setHasReturnValue(this.hasReturnValue);
                    program.updateFunction(function);
                    hasReturnValue = false;

                    if(isEnd()){
                        function = null;
                        return true;
                    }else{
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
            if (isExp()){
                if (isRightBracket()){
                    if (isAssign()){
                        if(isExp()) {
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
                if (isExp()) {
                    return true;
                }
            } else {
                printError(503);
            }
        } else if (isExp()) {
            return true;
        }

        return false;
    }


    private boolean isName() {
        if (currentToken.id != 100) return false;
        return true;
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

    private boolean isString() {
        if (currentToken.id != 128) return false;
        getNextToken();
        return true;
    }

    private boolean isColon() {
        if (currentToken.id != 123) return false;
        getNextToken();
        return true;
    }

    private boolean isNumber() {
        if (currentToken.id != 101) return false;
        getNextToken();
        return true;
    }

    private boolean isTrue() {
        if (currentToken.id != 218) return false;
        getNextToken();
        return true;
    }

    private boolean isFalse() {
        if (currentToken.id != 206) return false;
        getNextToken();
        return true;
    }

    private boolean isNil() {
        if (currentToken.id != 212) return false;
        getNextToken();
        return true;
    }


    private boolean isLocal() {
        if (currentToken.id != 211) return false;
        getNextToken();
        return true;
    }

    private boolean isIn() {
        if (currentToken.id != 210) return false;
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


    private boolean isRightParenthesis(){
        if (currentToken.id != 117) return false;
        getNextToken();
        return true;
    }

    private boolean isLeftParenthesis(){
        if (currentToken.id != 116) return false;
        getNextToken();
        return true;
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

    private boolean isHash() {
        if (currentToken.id != 108) return false;
        getNextToken();
        return true;
    }

    private boolean isNot() {
        if (currentToken.id != 213) return false;
        getNextToken();
        return true;
    }

    private boolean isOr() {
        if (currentToken.id != 214) return false;
        getNextToken();
        return true;
    }

    private boolean isAnd() {
        if (currentToken.id != 200) return false;
        getNextToken();
        return true;
    }

    private boolean isDifferent() {
        if (currentToken.id != 110) return false;
        getNextToken();
        return true;
    }

    private boolean isEqual() {
        if (currentToken.id != 109) return false;
        getNextToken();
        return true;
    }

    private boolean isGreaterOrEqualThan() {
        if (currentToken.id != 112) return false;
        getNextToken();
        return true;
    }

    private boolean isGreaterThan() {
        if (currentToken.id != 114) return false;
        getNextToken();
        return true;
    }

    private boolean isLessOrEqualThan() {
        if (currentToken.id != 111) return false;
        getNextToken();
        return true;
    }

    private boolean isLessThan() {
        if (currentToken.id != 113) return false;
        getNextToken();
        return true;
    }

    private boolean isDoubleDot() {
        if (currentToken.id != 126) return false;
        getNextToken();
        return true;
    }

    private boolean isTripleDot() {
        if (currentToken.id != 127) return false;
        getNextToken();
        return true;
    }

    private boolean isPercent() {
        if (currentToken.id != 106) return false;
        getNextToken();
        return true;
    }

    private boolean isCircumflexAccent() {
        if (currentToken.id != 107) return false;
        getNextToken();
        return true;
    }

    private boolean isSlash() {
        if (currentToken.id != 105) return false;
        getNextToken();
        return true;
    }

    private boolean isAsterisk() {
        if (currentToken.id != 104) return false;
        getNextToken();
        return true;
    }

    private boolean isMinus() {
        if (currentToken.id != 103) return false;
        getNextToken();
        return true;
    }

    private boolean isPlus() {
        if (currentToken.id != 102) return false;
        getNextToken();
        return true;

    }

    public void getNextToken() {
        try {
            currentToken = iterator.next();
        }
        catch (NoSuchElementException exception) {
            currentToken = null;
        }
    }

    public Token peekPreviousToken() {
        iterator.previous();
        Token previous = iterator.previous();
        iterator.next();
        iterator.next();
        return previous;
    }

}

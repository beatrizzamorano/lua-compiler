import javax.swing.*;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by beatrizzamorano on 1/1/17.
 */
public class SyntacticalAnalysis {
    private List<Token> tokens;
    private ListIterator<Token> iterator;
    private Token currentToken;
    private String errorStack = "";

    public SyntacticalAnalysis(List<Token> tokens) {
        this.tokens = tokens;
        this.iterator = tokens.listIterator();
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
            while(isStat()){
                isSemicolon();
            }
            if(isLastStat()){
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
		 local function Name funcbody |
		 local namelist [`=´ explist] */


    private boolean isStat(){
        if(isVarList()){
            if(isAssign()){
                if(isExpList()){
                    return true;
                }
            }else {
                printError(503);
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
        }else if(isFor()){
            if(isName()){
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
                    printError(503);
                }
            } else if(isNameList()){
                if(isIn()){
                    if(isExpList()){
                        if(isDo()){
                            if(isBlock()){
                                if(isEnd()){
                                    return true;
                                } else {
                                    printError(513);
                                }
                            }
                        }else {
                            printError(515);
                        }
                    }
                } else {
                    printError(518);
                }
            } else {
                printError(511);
                return false;
            }

        }else if(isFunction()){
            if(isFuncName()){
                if(isFuncBody()){
                    return true;
                }
            }
        }else if(isLocal()){
            if(isFunction()){
                if(isName()){
                    if(isFuncBody()){
                        return true;
                    }
                } else {
                    printError(511);
                }
            } else if(isNameList()){
                if(isAssign()){
                    if (!isExpList()) {
                        return false;
                    }
                } else {
                    printError(503);
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
                return true;
            }
            return true;

        }else if(isBreak()){
            return true;
        }

        return false;
    }



    //funcname ::= Name {`.´ Name} [`:´ Name]
    private boolean isFuncName(){
        if(isName()){
            while (isDot()){
                if(!isName()){
                    printError(511);
                    return false;
                }
            }
            if(isColon()){
                if(isName()){
                    return true;
                }else {
                    printError(511);
                }
            }
            return true;
        }

        return false;
    }

    //var ::=  Name | prefixexp `[´ exp `]´ | prefixexp `.´ Name

    private boolean isVar(){
        if(isName()){
            if(isVarTail()) {
                return true;
            }
        }else if(isPrefixExp()){
            if(isLeftBracket()){
                if(isExp()){
                    if(isRightBracket()){
                        if(isVarTail()) {
                            return true;
                        }
                    }else {
                        printError(504);
                    }
                }
            }else if(isDot()){
                if(isName()){
                    if(isVarTail()) {
                        return true;
                    }
                }else {
                    printError(511);
                }
            }else{
                printError(509);
            }
        }
        //else if (isFunctionCall()) {
        //    return true;
        //}

        return false;
    }

    private boolean isVarTail() {
        if (isLeftBracket()) {
            if (isExp()) {
                if (isRightBracket()) {
                    if (isVarTail()) {
                        return true;
                    }
                }
            }
        }

        if (isDot()) {
            if (isName()) {
                if (isVarTail()) {
                    return true;
                }
            }
        }

        return true;
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
        } else if (isVar()) {
            if (isExpTail()) {
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

    private boolean isVarList(){
        if(isVar()){
            while(isComma()){
                if(!isVar()){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //prefixexp ::= var | functioncall | `(´ exp `)´

    private boolean isPrefixExp() {
        if(isLeftParenthesis()){
            if(isExp()){
                if(isRightParenthesis()){
                    return true;
                }else printError(507);
            }
        }

        return false;
    }


    //functioncall ::=  prefixexp args | prefixexp `:´ Name args

    private boolean isFunctionCall(){
        if (isVar()) {
            if (isArgs()) {
                if (isFunctionCallTail()) {
                    return true;
                }
            } else if (isColon()) {
                if (isName()) {
                    if (isArgs()) {
                        if (isFunctionCallTail()) {
                            return true;
                        }
                    }
                }
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
                            if (isArgs()) {
                                if (isFunctionCallTail()) {
                                    return true;
                                }
                            }
                        }
                    }
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
                if (isArgs()) {
                    if (isFunctionCallTail()) {
                        return true;
                    }
                }
            }
        }

        return true;
    }

    //args ::=  `(´ [explist] `)´ | tableconstructor | String

    private boolean isArgs(){
        if(isLeftParenthesis()){
            if(isExpList()){
                if(isRightParenthesis()){
                    return true;
                }
            } else if(isRightParenthesis()) {
                return true;
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
        if(isLeftParenthesis()){
            if(isParList()){
                if(isRightParenthesis()){
                    if(isBlock()){
                        if(isEnd()){
                            return true;
                        }else{
                            printError(513);
                        }
                    }
                } else {
                    printError(507);
                }
            } else if(isRightParenthesis()){
                if(isBlock()){
                    if(isEnd()){
                        return true;
                    }else{
                        printError(513);
                    }
                }
            }
        }

        return false;
    }


    //parlist ::= namelist [`,´ `...´] | `...´


    private boolean isParList(){
        if(isNameList()) {
            if (isComma()) {
                if(isTripleDot()){
                    return true;
                } else{
                    printError(512);
                }
            }
            return true;
        }
        else if(isTripleDot()) {
            return true;
        }

        return false;
    }

    //namelist ::= Name {`,´ Name}

    private boolean isNameList(){
        if(isName()){
            while (isComma()) {
                if (!isName()) {
                    printError(511);
                    return false;
                }
            }
            return true;
        }

        return false;
    }


    //tableconstructor ::= `{´ [fieldlist] `}´

    private boolean isTableConstructor(){
        if(isLeftCurlyBracket()){
            if(isFieldList()){
                if(isRightCurlyBracket()){
                    return true;

                }else printError(505);

            }else if(isRightCurlyBracket()) {
                return true;
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
        } else {
            if (isName()) {
                if (isAssign()) {
                    if (isExp()) {
                        return true;
                    }
                } else {
                    printError(503);
                }
            } else if (isExp()) return true;
        }

        return false;
    }


    private boolean isName() {
        if (currentToken.id != 100) return false;
        getNextToken();
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

}

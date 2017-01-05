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
    private String errorStack;

    public SyntacticalAnalysis(List<Token> tokens) {
        this.tokens = tokens;
        tokens.listIterator();
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
    //tableconstructor ::= `{´ [fieldlist] `}´

    private boolean isTableConstructor(){
        if(isLeftCurlyBracket()){
            if(isFieldList()){
                if(isRightCurlyBracket()){
                    return true;

                }else printError(505);

            }else if(isRightCurlyBracket()) return true;
        }

        return false;
    }




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
                        if(isExp()) return true;

                    } else {
                        printError(503);
                    }
                } else {
                   printError(504);
                }
            }
        } else if(isName()){
            if(isAssign()){
                if(isExp()) return true;
            }
            else{
                printError(503);
            }
        } else if(isExp()) return true;

        return false;
    }

    private boolean isName() {
        if (currentToken.lineNumber != 100) return false;
        getNextToken();
        return true;
    }

    private boolean isAssign() {
        if (currentToken.lineNumber != 115) return false;
        getNextToken();
        return true;
    }

    private boolean isRightBracket() {
        if (currentToken.lineNumber != 121) return false;
        getNextToken();
        return true;
    }

    private boolean isExp() {
        //TODO
        return false;
    }

    private boolean isLeftCurlyBracket() {
        if (currentToken.lineNumber != 118) return false;
        getNextToken();
        return true;
    }

    private boolean isRightCurlyBracket() {
        if (currentToken.lineNumber != 119) return false;
        getNextToken();
        return true;
    }

    private boolean isLeftBracket() {
        if (currentToken.lineNumber != 120) return false;
        getNextToken();
        return true;
    }

    private boolean isSemicolon() {
        if (currentToken.lineNumber != 122) return false;
        getNextToken();
        return true;
    }

    private boolean isComma() {
        if (currentToken.lineNumber != 124) return false;
        getNextToken();
        return true;
    }

    private boolean isHash() {
        if (currentToken.lineNumber != 108) return false;
        getNextToken();
        return true;
    }

    private boolean isNot() {
        if (currentToken.lineNumber != 213) return false;
        getNextToken();
        return true;
    }

    private boolean isOr() {
        if (currentToken.lineNumber != 214) return false;
        getNextToken();
        return true;
    }

    private boolean isAnd() {
        if (currentToken.lineNumber != 200) return false;
        getNextToken();
        return true;
    }

    private boolean isDifferent() {
        if (currentToken.lineNumber != 110) return false;
        getNextToken();
        return true;
    }

    private boolean isEqual() {
        if (currentToken.lineNumber != 109) return false;
        getNextToken();
        return true;
    }

    private boolean isGreaterOrEqualThan() {
        if (currentToken.lineNumber != 112) return false;
        getNextToken();
        return true;
    }

    private boolean isGreaterThan() {
        if (currentToken.lineNumber != 114) return false;
        getNextToken();
        return true;
    }

    private boolean isLessOrEqualThan() {
        if (currentToken.lineNumber != 111) return false;
        getNextToken();
        return true;
    }

    private boolean isLessThan() {
        if (currentToken.lineNumber != 113) return false;
        getNextToken();
        return true;
    }

    private boolean isDoubleDot() {
        if (currentToken.lineNumber != 126) return false;
        getNextToken();
        return true;
    }

    private boolean isPercent() {
        if (currentToken.lineNumber != 106) return false;
        getNextToken();
        return true;
    }

    private boolean isCircumflexAccent() {
        if (currentToken.lineNumber != 107) return false;
        getNextToken();
        return true;
    }

    private boolean isSlash() {
        if (currentToken.lineNumber != 105) return false;
        getNextToken();
        return true;
    }

    private boolean isAsterisk() {
        if (currentToken.lineNumber != 104) return false;
        getNextToken();
        return true;
    }

    private boolean isMinus() {
        if (currentToken.lineNumber != 103) return false;
        getNextToken();
        return true;
    }

    private boolean isPlus() {
        if (currentToken.lineNumber != 102) return false;
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

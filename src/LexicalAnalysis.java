import javax.swing.*;
import java.util.LinkedList;

/**
 * Created by beatrizzamorano on 11/4/16.
 */
public class LexicalAnalysis {
    private static LinkedList<Token> tokens;
    private LexicalStates lexicalStates;
    private String code;

    public LexicalAnalysis(){
        lexicalStates = new LexicalStates();

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String printTokens() {
        String tokensString = "";
        for (Token token : tokens) {
            tokensString += token.toString() + "\n";
        }
        return tokensString;
    }

    public boolean generateTokens(){
        tokens = new LinkedList<>();
        int lineNumber = 1;
        int state = 0;
        String lexeme = "";
        String key = "";
        char currentChar = ' ';
        for(int i = 0; i <= code.length(); i++){

            if(i == code.length()){
                key = "EOF";
            }else {
                 currentChar = code.charAt(i);
                 key = getCharacterKey(currentChar);
            }

            if (currentChar == '\n') lineNumber++;
            int newState = lexicalStates.getNextState(state, key);

            if (newState == 0){
                state = 0;
                lexeme = "";
            } else if (newState < 100){
                lexeme += currentChar;
                state = newState;
            } else if (newState >= 100 && newState < 500){
                if (isIndexDecrementRequired(newState)){
                    i--;
                    if(currentChar == '\n'){
                        lineNumber--;
                    }
                } else {
                    lexeme += currentChar;
                }
                if (newState == 100) newState = searchKeyword(lexeme);
                Token token = new Token(lexeme, newState, lineNumber);
                tokens.add(token);
                state = 0;
                lexeme = "";
            } else if (newState >= 500) {
                printError(newState, lineNumber);
                return false;
            }
        }

        System.out.println(tokens);

        return true;
    }

    private boolean isIndexDecrementRequired(int state){
        switch (state){
            case 100: return true;
            case 101: return true;
            case 103: return true;
            case 115: return true;
            case 113: return true;
            case 114: return true;
            case 125: return true;
            case 126: return true;

        }
        return state >= 200 && state < 500;
    }


    private String getCharacterKey(char character){
        if (Character.isLetter(character) || character == '_') return "Alpha";
        if (Character.isDigit(character)) return "Digit";
        return character + "";

    }

    private int searchKeyword(String lexeme){
        switch (lexeme){
            case "and": return 200;
            case "break": return 201;
            case "do": return 202;
            case "else": return 203;
            case "elseif": return 204;
            case "end": return 205;
            case "false": return 206;
            case "for": return 207;
            case "function": return 208;
            case "if": return 209;
            case "in": return 210;
            case "local": return 211;
            case "nil": return 212;
            case "not": return 213;
            case "or": return 214;
            case "repeat": return 215;
            case "return": return 216;
            case "then": return 217;
            case "true": return 218;
            case "until": return 219;
            case "while": return 220;
            default: return 100;
        }
    }


    private void printError(int errorNumber, int lineNumber){
        String message = getError(errorNumber) + "\nLinea: " + lineNumber;
        JOptionPane.showMessageDialog(null, message);
    }

    private String getError(int errorNumber){
        switch (errorNumber){
            case 500: return "Error 500. Invalid character.";
            case 501: return "Error 501. Unexpected end of file.";
            case 502: return "Error 502. Unexpected new line.";
            default: return "Error " + errorNumber + ".";
        }
    }
}

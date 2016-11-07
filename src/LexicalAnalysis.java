import javax.swing.*;
import java.util.LinkedList;

/**
 * Created by beatrizzamorano on 11/4/16.
 */
public class LexicalAnalysis {
    public static LinkedList<Token> tokens;
    private LexicalStates lexicalStates;
    public String code;

    public LexicalAnalysis(){
        lexicalStates = new LexicalStates();

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean generateTokens(){
        tokens = new LinkedList<>();
        int lineNumber = 1;
        int state = 0;
        String lexeme = "";
        for(int i = 0; i<code.length(); i++){
            char currentChar = code.charAt(i);
            if (currentChar == '\n') lineNumber++;
            String key = getCharacterKey(currentChar);
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

    private int getPossibleEndOfFileError(int newState) {
        switch (newState) {
            case 3: return 501;
            case 8: return 501;
            case 16: return 501;
            case 17: return 501;
            case 18: return 501;
            case 19: return 501;
            default: return newState;
        }
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
            case 500: return "Error 500. Caracter no válido.";
            case 501: return "Error 501. Fin de archivo inesperado.";
            case 502: return "Error 502. Nueva linea inesperada.";
            default: return "Error " + errorNumber + ".";
        }
    }
}
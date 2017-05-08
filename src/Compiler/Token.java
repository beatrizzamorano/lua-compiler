package Compiler;

/**
 * Created by beatrizzamorano on 11/4/16.
 */
public class Token {
    public String lexeme;
    public int id;
    public int lineNumber;


    public Token (String lexeme, int id, int lineNumber){
        this.lexeme = lexeme;
        this.id = id;
        this.lineNumber = lineNumber;

    }

    @Override
    public String toString() {
        return "L: " + this.lineNumber + ", ID: " + this.id + ", " + this.lexeme;
    }
}

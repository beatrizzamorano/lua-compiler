/**
 * Created by beatrizzamorano on 3/15/17.
 */
public class Parameters {
    private int id;
    private TypeEnum type;
    private String lexeme;



    public Parameters(int id, TypeEnum type, String lexeme) {
        this.id = id;
        this.type = type;
        this.lexeme = lexeme;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

}

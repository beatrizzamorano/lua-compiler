

/**
 * Created by beatrizzamorano on 3/12/17.
 */
public class Variable {
    private int id;
    private TypeEnum type;
    private String name;
    Token token;


    //hashcode
    //private String valor;
    //private String direccion;



    public Variable(String name) {
        this.name = name;
    }

    public Variable(String name, TypeEnum type, Token token, int id) {
        this.name = name;
        this.type = type;
        this.token = token;
        this.id = this.hashCode();

    }





    public TypeEnum getType() {
        return this.type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token getToken() { return token; }

    public void setToken(Token token) { this.token = token; }



}

import java.util.List;

/**
 * Created by beatrizzamorano on 3/12/17.
 */
public class Variable {
    private int id;
    private TypeEnum type;
    private String name;
    private Object value;
    private List<Variable> variables;

    //hashcode
    //private String valor;
    //private String direccion;


    public Variable(String name) {
        this.name = name;
    }

    public Variable(String name, TypeEnum type) {
        this.name = name;
        this.type = type;
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

    public void setValue(Object value) { this.value = value; }

}

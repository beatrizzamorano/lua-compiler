/**
 * Created by beatrizzamorano on 3/15/17.
 */
public class Parameter {
    private int id;
    private TypeEnum type;
    private String name;

    public Parameter(String name) {
        this.id = hashCode();
        this.type = TypeEnum.NIL;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

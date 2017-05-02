import sun.jvm.hotspot.debugger.cdbg.EnumType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by beatriz zamorano on 21/03/17.
 */
public class Function {
    private HashMap<Integer, Parameter> parameters;
    private HashMap<Integer, Variable> localVariables;
    private boolean hasReturnValue;
    private String name;

    public Function() {
        this.parameters = new HashMap<>();
        this.localVariables = new HashMap<>();
        this.hasReturnValue = false;
    }

    public void setHasReturnValue(boolean hasReturnValue) {
        this.hasReturnValue = hasReturnValue;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addLocalVariable(Variable variable) {
        this.localVariables.put(variable.hashCode(), variable);
    }

    public void addParameters(List<Parameter> params) {
        for (Parameter param : params) {
            this.parameters.put(param.hashCode(), param);
        }
    }
}

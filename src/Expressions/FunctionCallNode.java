package Expressions;

import Compiler.TypeEnum;

public class FunctionCallNode implements Node {
    private String functionName;


    public FunctionCallNode(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public TypeEnum getType() {
        return null;
    }

    public String getFunctionName() {
        return this.functionName;
    }

}

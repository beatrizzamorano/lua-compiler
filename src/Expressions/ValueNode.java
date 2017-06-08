package Expressions;

import Compiler.*;

public class ValueNode implements Node {
    Token token;

    public ValueNode(Token token) {
        this.token = token;
    }

    @Override
    public TypeEnum getType() {
        switch (this.token.id) {
            case 101: return TypeEnum.NUMBER;
            case 128: return TypeEnum.STRING;
            case 206:
            case 218: return TypeEnum.BOOLEAN;
            case 212: return TypeEnum.NIL;
            default: return TypeEnum.VOID;
        }
    }

}

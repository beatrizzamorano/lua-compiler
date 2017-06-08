package Expressions;

import Compiler.TypeEnum;

public class BaseOperator implements Operator, Node {
    private final String symbol;
    private final boolean isRightAssociative;
    private final int precedence;

    public BaseOperator(String symbol, boolean rightAssociative, int precedence) {
        this.symbol = symbol;
        this.isRightAssociative = rightAssociative;
        this.precedence = precedence;
    }

    @Override
    public boolean isRightAssociative() {
        return this.isRightAssociative;
    }

    @Override
    public int comparePrecedence(Operator o) {
        if (o instanceof BaseOperator) {
            BaseOperator other = (BaseOperator) o;
            if (precedence > other.precedence) {
                return 1;
            } else if (other.precedence == precedence) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -o.comparePrecedence(this);
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public TypeEnum getType() {
        return TypeEnum.OPERATOR;
    }
}

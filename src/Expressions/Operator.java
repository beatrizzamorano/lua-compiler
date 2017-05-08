package Expressions;

/**
 * Created by beatriz zamorano on 21/03/17.
 */
public interface Operator {
    boolean isRightAssociative();

    int  comparePrecedence(Operator o);

    String getSymbol();
}

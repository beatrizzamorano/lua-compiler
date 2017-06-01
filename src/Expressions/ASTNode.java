package Expressions;
import Statements.Statement;

/**
 * Created by beatriz zamorano on 21/03/17.
 */
public class ASTNode {
    private final Object value;
    private final ASTNode leftASTNode;
    private final ASTNode rightASTNode;

    public ASTNode(Object value, ASTNode leftASTNode, ASTNode rightASTNode) {
        this.value = value;
        this.leftASTNode = leftASTNode;
        this.rightASTNode = rightASTNode;
    }

    public Object getValue() {
        return value;
    }

    public ASTNode getLeftASTNode() {
        return leftASTNode;
    }

    public ASTNode getRightASTNode() {
        return rightASTNode;
    }
}

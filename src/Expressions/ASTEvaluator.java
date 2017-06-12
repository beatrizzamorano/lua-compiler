package Expressions;

import Compiler.*;

/**
 * Created by beatriz zamorano on 21/03/17.
 */
public class ASTEvaluator {

    public TypeEnum evaluateAST(ASTNode tree) throws SemanthicException {
        TypeEnum typeLeft, typeRight;
        Node value = tree.getValue();

        if (value instanceof Variable) {
            Program program = Program.getInstance();
            Variable savedVariable = program.getGlobalVariable((Variable) value);
            return savedVariable.getType();
        }
        else if (value instanceof BaseOperator) {
            switch (((BaseOperator) value).getSymbol()) {
                case "/":
                case "-":
                case "*":
                    typeLeft = evaluateAST(tree.getLeftASTNode());
                    typeRight = evaluateAST(tree.getRightASTNode());
                    if (typeLeft == typeRight && typeLeft == TypeEnum.NUMBER) return TypeEnum.NUMBER;
                    else throw new SemanthicException(528);
                case "+":
                    typeLeft = evaluateAST(tree.getLeftASTNode());
                    typeRight = evaluateAST(tree.getRightASTNode());
                    if (typeLeft == typeRight) {
                        if (typeLeft == TypeEnum.NUMBER) {
                            return TypeEnum.NUMBER;
                        }

                        if (typeLeft == TypeEnum.STRING) {
                            return TypeEnum.STRING;
                        }
                    }
                    throw new SemanthicException(528);
                case "and":
                case "or":
                    typeLeft = evaluateAST(tree.getLeftASTNode());
                    typeRight = evaluateAST(tree.getRightASTNode());
                    if (typeLeft == typeRight && typeLeft == TypeEnum.BOOLEAN) {
                        return TypeEnum.BOOLEAN;
                    } else {
                        throw new SemanthicException(528);
                    }
                case "~=":
                case "==":
                    typeLeft = evaluateAST(tree.getLeftASTNode());
                    typeRight = evaluateAST(tree.getRightASTNode());
                    if (typeLeft == typeRight) return TypeEnum.BOOLEAN;
                    throw new SemanthicException(528);
                case "<":
                case ">":
                case "<=":
                case ">=":
                    typeLeft = evaluateAST(tree.getLeftASTNode());
                    typeRight = evaluateAST(tree.getRightASTNode());
                    if (typeLeft == typeRight && typeLeft == TypeEnum.NUMBER) {
                        return TypeEnum.BOOLEAN;
                    } else {
                        throw new SemanthicException(528);
                    }
                case "=":
                    typeRight = evaluateAST(tree.getRightASTNode());
                    Node variableNode = tree.getLeftASTNode().getValue();

                    Program program = Program.getInstance();

                    if (variableNode instanceof Variable) {
                        Variable mockVariable = (Variable) variableNode;
                        Variable savedVariable = program.getGlobalVariable(mockVariable);
                        savedVariable.setType(typeRight);

                        return typeRight;
                    } else {
                        throw new SemanthicException(528);
                    }


            }
        } else if (value instanceof FunctionCallNode) {

        }

        return value.getType();
    }
}

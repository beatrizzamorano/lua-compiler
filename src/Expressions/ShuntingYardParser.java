package Expressions;

import java.util.*;

import Compiler.Token;

public class ShuntingYardParser {
    private final Map<String, BaseOperator> operators;

    public ShuntingYardParser() {
        OperatorFactory operatorFactory = new OperatorFactory();
        this.operators = operatorFactory.getOperators();
    }

    private static void addNode(Stack<ASTNode> stack, BaseOperator operator) {
        final ASTNode rightASTNode = stack.pop();
        final ASTNode leftASTNode = stack.pop();
        stack.push(new ASTNode(operator, leftASTNode, rightASTNode));
    }


     public ASTNode convertInfixNotationToAST(final List<Node> input){
        final Stack<BaseOperator> operatorStack = new Stack<>();
        final Stack<ASTNode> operandStack = new Stack<>();

        main:
        for (Node n : input) {
            BaseOperator popped;

            if (n instanceof BaseOperator) {
                BaseOperator operator = (BaseOperator) n;

                switch (operator.getSymbol()) {
                    case "(":
                        operatorStack.push(operator);
                        break;
                    case ")":
                        while (!operatorStack.isEmpty()) {
                            popped = operatorStack.pop();

                            if (Objects.equals(popped.getSymbol(), "(")) {
                                continue  main;
                            } else {
                                addNode(operandStack, popped);
                            }
                        }

                        throw new IllegalStateException("Unbalanced right parentheses");
                    default:
                        final Operator newOperator = operators.get(operator.getSymbol());
                        Operator newOperator2;

                        while (!operatorStack.isEmpty()
                                && null != (newOperator2 = operators.get(operatorStack.peek().getSymbol())))
                        {
                            if ((!newOperator.isRightAssociative()
                                    && 0 == newOperator.comparePrecedence(newOperator2))
                                    || newOperator.comparePrecedence(newOperator2) < 0)
                            {
                                BaseOperator baseOperator = operatorStack.pop();
                                addNode(operandStack, baseOperator);
                            } else {
                                break;
                            }

                        }
                        operatorStack.push((BaseOperator) n);

                }
            } else {
                operandStack.push(new ASTNode(n, null, null));
            }
        }
        while (!operatorStack.isEmpty()){
                addNode(operandStack, operatorStack.pop());
            }
        if (operandStack.isEmpty()) return null;
        return operandStack.pop();
    }
}

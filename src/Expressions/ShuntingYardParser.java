package Expressions;

import java.util.*;

import Compiler.Token;


/**
 * Created by beatriz zamorano on 21/03/17.
 */
public class ShuntingYardParser {
    private final Map<String, Operator> operators;

    public ShuntingYardParser() {
        this.operators = new HashMap<>();
        this.operators.put("*", new BaseOperator("*", false, 15));
        this.operators.put("/", new BaseOperator("/", false, 15));
        this.operators.put("+", new BaseOperator("+", false, 7));
        this.operators.put("-", new BaseOperator("-", false, 7));
        this.operators.put("<", new BaseOperator("<", false, 6));
        this.operators.put("<=", new BaseOperator("<=", false, 6));
        this.operators.put(">", new BaseOperator(">", false, 6));
        this.operators.put(">=", new BaseOperator(">=", false, 6));
        this.operators.put("==", new BaseOperator("==", false, 5));
        this.operators.put("~=", new BaseOperator("!=", false, 5));
        this.operators.put("and", new BaseOperator("and", false, 4));
        this.operators.put("or", new BaseOperator("or", false, 4));
        this.operators.put("=", new BaseOperator("=", true, 3));
    }

    private static void addNode(Stack<ASTNode> stack, Token operator) {
        final ASTNode rightASTNode = stack.pop();
        final ASTNode leftASTNode = stack.pop();
        stack.push(new ASTNode(operator, leftASTNode, rightASTNode));
    }


     public ASTNode convertInfixNotationToAST(final List<Token> input){
                final Stack<Token> operatorStack = new Stack<>();
                final Stack<ASTNode> operandStack = new Stack<>();

                main:
                for (Token t : input){
                    Token popped;
                    switch(t.lexeme) {
                        case " ":
                            break;
                        case "(":
                            operatorStack.push(t);
                            break;
                        case ")":
                            while(!operatorStack.isEmpty()){
                                popped = operatorStack.pop();
                                if (Objects.equals("(", popped.lexeme)){
                                    continue main;
                                } else {
                                    addNode(operandStack, popped);
                                }
                            }
                            throw new IllegalStateException("Unbalanced right parentheses");
                        default:
                            if(operators.containsKey(t.lexeme)){
                                final Operator operator = operators.get(t.lexeme);
                                Operator operator2;
                                while(!operatorStack.isEmpty() &&
                                    null != (operator2 = operators.get(operatorStack.peek().lexeme))){
                                    if ((!operator.isRightAssociative()
                                        && 0 == operator.comparePrecedence(operator2))
                                        || operator.comparePrecedence(operator2) < 0) {
                                        Token token = operatorStack.pop();
                                        addNode(operandStack, token);
                                    } else {
                                        break;
                                    }
                                }
                                operatorStack.push(t);
                            } else {
                                operandStack.push(new ASTNode(t, null, null));
                            }
                            break;
                        }
                    }
                while (!operatorStack.isEmpty()){
                        addNode(operandStack, operatorStack.pop());
                    }
                if (operandStack.isEmpty()) return null;
                return operandStack.pop();
            }
}

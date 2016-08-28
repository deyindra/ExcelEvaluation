package org.idey.excel.expression.rpn;

import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.expression.operator.AbstractOperator;
import org.idey.excel.expression.tokenizer.AbstractExpressionToken;
import org.idey.excel.expression.tokenizer.ExpressionTokenizer;
import org.idey.excel.expression.tokenizer.OperatorExpressionToken;
import org.idey.excel.expression.tokenizer.TokenEnum;

import java.util.*;

/**
 * @author
 * Class to convert infix to reverse polish notation
 */
public class RPN {


    private static boolean checkBalancedParenthesis(String expression){
        final Map<Character,Character> map = new HashMap<>();
        map.put(')','(');
        map.put('}','{');
        map.put(']','[');

        Set<Character> values = new HashSet<>(map.values());
        LinkedList<Character> stack = new LinkedList<>();

        for(int i=0;i<expression.length();i++){
            char ch = expression.charAt(i);
            if(values.contains(ch)){
                stack.push(ch);
            }else{
                if(map.containsKey(ch)){
                    if(stack.isEmpty()){
                        return false;
                    }else{
                        char value = map.get(ch);
                        char top = stack.peek();
                        if(value == top){
                            stack.pop();
                        }else{
                            return false;
                        }
                    }
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * Convert a Set of tokens from infix to reverse polish notation
     * @param expression the expression to convert
     * @param userFunctions the custom functions used
     * @param userOperators the custom operators used
     * @param variableNames the variable names used in the expression
     * @return a {@link AbstractExpressionToken} array containing the result
     */
    public static AbstractExpressionToken[] infixToRPN(String expression, final Map<String, AbstractFunction> userFunctions,
                                                       final Map<String, AbstractOperator> userOperators, final Set<String> variableNames){

        if(expression==null || expression.trim().equals("")){
            throw new IllegalArgumentException("Invalid Expression");
        }else {
            expression = expression.trim();
            boolean isBalancedParenthesis = checkBalancedParenthesis(expression);
            if(!isBalancedParenthesis){
                throw new IllegalArgumentException(String.format("%s does not have balanced parenthesis",expression ));
            }
            final Stack<AbstractExpressionToken> stack = new Stack<>();
            final List<AbstractExpressionToken> output = new ArrayList<>();

            final ExpressionTokenizer tokenizer = new ExpressionTokenizer.TokenizerBuilder(expression)
                    .withCustomUserFunctions(userFunctions)
                    .withCustomUserOperators(userOperators)
                    .withVariableNames(variableNames).build();
            while (tokenizer.hasNext()) {
                AbstractExpressionToken token = tokenizer.next();
                switch (token.getType()) {
                    case TOKEN_NUMBER:
                    case TOKEN_VARIABLE:
                        output.add(token);
                        break;
                    case TOKEN_FUNCTION:
                        stack.add(token);
                        break;
                    case TOKEN_SEPARATOR:
                        while (!stack.isEmpty() && stack.peek().getType() != TokenEnum.TOKEN_PARENTHESES_OPEN) {
                            output.add(stack.pop());
                        }
                        if (stack.isEmpty() || stack.peek().getType() != TokenEnum.TOKEN_PARENTHESES_OPEN) {
                            throw new IllegalArgumentException("Misplaced function separator ',' or mismatched parentheses");
                        }
                        break;
                    case TOKEN_OPERATOR:
                        while (!stack.isEmpty() && stack.peek().getType() == TokenEnum.TOKEN_OPERATOR) {
                            OperatorExpressionToken o1 = (OperatorExpressionToken) token;
                            OperatorExpressionToken o2 = (OperatorExpressionToken) stack.peek();
                            if (o1.getOperator().getNumOperands() == 1 && o2.getOperator().getNumOperands() == 2) {
                                break;
                            } else if ((o1.getOperator().isLeftAssociative() && o1.getOperator().getPrecedence() <= o2.getOperator().getPrecedence())
                                    || (o1.getOperator().getPrecedence() < o2.getOperator().getPrecedence())) {
                                output.add(stack.pop());
                            } else {
                                break;
                            }
                        }
                        stack.push(token);
                        break;
                    case TOKEN_PARENTHESES_OPEN:
                        stack.push(token);
                        break;
                    case TOKEN_PARENTHESES_CLOSE:
                        while (stack.peek().getType() != TokenEnum.TOKEN_PARENTHESES_OPEN) {
                            output.add(stack.pop());
                        }
                        stack.pop();
                        if (!stack.isEmpty() && stack.peek().getType() == TokenEnum.TOKEN_FUNCTION) {
                            output.add(stack.pop());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown Token type encountered. This should not happen");
                }
            }
            while (!stack.isEmpty()) {
                AbstractExpressionToken t = stack.pop();
                if (t.getType() == TokenEnum.TOKEN_PARENTHESES_CLOSE || t.getType() == TokenEnum.TOKEN_PARENTHESES_OPEN) {
                    throw new IllegalArgumentException("Mismatched parentheses detected. Please check the expression");
                } else {
                    output.add(t);
                }
            }
            return output.toArray(new AbstractExpressionToken[output.size()]);
        }
    }
}

package org.idey.excel.expression.tokenizer;

import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.expression.function.BuiltInFunctions;
import org.idey.excel.expression.operator.AbstractOperator;
import org.idey.excel.expression.operator.AllowedCharsetUtil;
import org.idey.excel.expression.operator.BuiltInOperators;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author i.dey
 * An {@link Iterator} implementation whcih take an String expression, parse it and return all available token
 * @see AbstractExpressionToken
 * @see Iterator
 * @see Iterable
 */
public class ExpressionTokenizer implements Iterator<AbstractExpressionToken>, Iterable<AbstractExpressionToken>{
    //char array of the passed expression
    private final char[] expression;
    private final int expressionLength;
    //custom user define function
    private final Map<String, AbstractFunction> userFunctions;
    //custom user define operators
    private final Map<String, AbstractOperator> userOperators;
    //user define variables
    private final Set<String> variableNames;
    private int pos = 0;
    //AbstractExpressionToken for maintain the state of last visited token
    private AbstractExpressionToken lastToken;

    /**
     *
     * @param builder {@link TokenizerBuilder} A builder class for building tokenizer
     */
    private ExpressionTokenizer(TokenizerBuilder builder) {
        this.expression = builder.expression;
        this.expressionLength = this.expression.length;
        this.userFunctions = builder.userFunctions;
        this.userOperators = builder.userOperators;
        this.variableNames = builder.variableNames;
    }

    /**
     *
     * @return true if expressionLength > pos
     */
    @Override
    public boolean hasNext() {
        return this.expressionLength > pos;
    }

    /**
     *
     * @return samen ExpressionTokenizer instance
     */
    @Override
    public Iterator<AbstractExpressionToken> iterator() {
        return this;
    }

    /**
     *
     * @return AbstractExpressionToken
     */
    @Override
    public AbstractExpressionToken next(){
        char ch = expression[pos];
        //check if ch is white space
        while (Character.isWhitespace(ch)) {
            ch = expression[++pos];
        }
        if (Character.isDigit(ch) || ch == '.') {
            if (lastToken != null) {
                if (lastToken.getType() == TokenEnum.TOKEN_NUMBER) {
                    throw new IllegalArgumentException("Unable to parse char '" + ch + "' (Code:" + (int) ch + ") at [" + pos + "]");
                } else if (( (lastToken.getType() != TokenEnum.TOKEN_OPERATOR ||
                        (lastToken.getType() == TokenEnum.TOKEN_OPERATOR &&
                        ((OperatorExpressionToken)lastToken).getOperator().isLeftAssociative() &&
                        ((OperatorExpressionToken)lastToken).getOperator().getNumOperands()==1))
                        && lastToken.getType() != TokenEnum.TOKEN_PARENTHESES_OPEN
                        && lastToken.getType() != TokenEnum.TOKEN_FUNCTION
                        && lastToken.getType() != TokenEnum.TOKEN_SEPARATOR)) {
                    // insert an implicit multiplication token
                    lastToken = new OperatorExpressionToken(BuiltInOperators.getBuiltinOperator('*', 2));
                    return lastToken;
                }
            }
            return parseNumberToken(ch);
        } else if (isArgumentSeparator(ch)) {
            return parseArgumentSeparatorToken();
        } else if (isOpenParentheses(ch)) {
            if (lastToken != null &&
                    ( (lastToken.getType() != TokenEnum.TOKEN_OPERATOR ||
                            (lastToken.getType() == TokenEnum.TOKEN_OPERATOR &&
                            ((OperatorExpressionToken)lastToken).getOperator().isLeftAssociative() &&
                            ((OperatorExpressionToken)lastToken).getOperator().getNumOperands()==1))
                            && lastToken.getType() != TokenEnum.TOKEN_PARENTHESES_OPEN
                            && lastToken.getType() != TokenEnum.TOKEN_FUNCTION
                            && lastToken.getType() != TokenEnum.TOKEN_SEPARATOR)) {
                // insert an implicit multiplication token
                lastToken = new OperatorExpressionToken(BuiltInOperators.getBuiltinOperator('*', 2));
                return lastToken;
            }
            return parseParentheses(true);
        } else if (isCloseParentheses(ch)) {
            return parseParentheses(false);
        } else if (AllowedCharsetUtil.isAllowedOperatorChar(ch)) {
            return parseOperatorToken(ch);
        } else if (isAlphabetic(ch) || ch == '_') {
            // parse the name which can be a setVariable or a function
            if (lastToken != null &&
                    ((lastToken.getType() != TokenEnum.TOKEN_OPERATOR ||
                            (lastToken.getType() == TokenEnum.TOKEN_OPERATOR &&
                             ((OperatorExpressionToken)lastToken).getOperator().isLeftAssociative() &&
                             ((OperatorExpressionToken)lastToken).getOperator().getNumOperands()==1))
                            && lastToken.getType() != TokenEnum.TOKEN_PARENTHESES_OPEN
                            && lastToken.getType() != TokenEnum.TOKEN_FUNCTION
                            && lastToken.getType() != TokenEnum.TOKEN_SEPARATOR)) {
                // insert an implicit multiplication token
                lastToken = new OperatorExpressionToken(BuiltInOperators.getBuiltinOperator('*', 2));
                return lastToken;
            }
            return parseFunctionOrVariable();

        }
        throw new IllegalArgumentException("Unable to parse char '" + ch + "' (Code:" + (int) ch + ") at [" + pos + "]");
    }

    /**
     *
     * @return {@link ArgumentSeparatorExpressionToken}
     */
    private AbstractExpressionToken parseArgumentSeparatorToken() {
        this.pos++;
        this.lastToken = new ArgumentSeparatorExpressionToken();
        return lastToken;
    }

    /**
     *
     * @param ch character from expression
     * @return true if ch == ','
     */
    private boolean isArgumentSeparator(char ch) {
        return ch == ',';
    }

    /**
     * @param open true if it is open parenthesis
     * @return either {@link OpenParenthesesExpressionToken} or {@link CloseParenthesesExpressionToken}
     */
    private AbstractExpressionToken parseParentheses(final boolean open) {
        if (open) {
            this.lastToken = new OpenParenthesesExpressionToken();
        } else {
            this.lastToken = new CloseParenthesesExpressionToken();
        }
        this.pos++;
        return this.lastToken;
    }

    /**
     *
     * @param ch character from expression
     * @return true if ch == '(' || ch == '{' || ch == '['
     */
    private boolean isOpenParentheses(char ch) {
        return ch == '(' || ch == '{' || ch == '[';
    }


    /**
     *
     * @param ch character from expression
     * @return true if ch == ')' || ch == '}' || ch == ']'
     */
    private boolean isCloseParentheses(char ch) {
        return ch == ')' || ch == '}' || ch == ']';
    }

    /**
     * @return either {@link FunctionExpressionToken} or {@link VariableExpressionToken}
     * @throws IllegalFunctionOrVariableException in case Function or Variable not not registered
     */
    private AbstractExpressionToken parseFunctionOrVariable() {
        final int offset = this.pos;
        int lastValidLen = 1;
        AbstractExpressionToken lastValidToken = null;
        int len = 1;
        if (isEndOfExpression(offset)) {
            this.pos++;
        }
        while (!isEndOfExpression(offset + len - 1) &&
                (isAlphabetic(expression[offset + len - 1]) ||
                        Character.isDigit(expression[offset + len - 1]) ||
                        expression[offset + len - 1] == '_')) {
            String name = new String(expression, offset, len);
            if (variableNames != null && variableNames.contains(name)) {
                lastValidLen = len;
                lastValidToken = new VariableExpressionToken(name);
            } else {
                final AbstractFunction f = getFunction(name);
                if (f != null) {
                    lastValidLen = len;
                    lastValidToken = new FunctionExpressionToken(f);
                }
            }
            len++;
        }
        if (lastValidToken == null) {
            throw new IllegalFunctionOrVariableException(new String(expression), pos, len);
        }
        pos += lastValidLen;
        lastToken = lastValidToken;
        return lastToken;
    }

    /**
     *
     * @param name function name
     * @return either user defined {@link AbstractFunction} or
     * built in {@link AbstractFunction} or null if not register
     */
    private AbstractFunction getFunction(String name) {
        AbstractFunction f = null;
        if (this.userFunctions != null) {
            f = this.userFunctions.get(name);
        }
        if (f == null) {
            f = BuiltInFunctions.getBuiltinFunction(name);
        }
        return f;
    }


    /**
     *
     * @param firstChar character for expression
     * @return {@link OperatorExpressionToken}
     * @throws IllegalArgumentException in case {@link AbstractOperator} is null
     */
    private AbstractExpressionToken parseOperatorToken(char firstChar) {
        final int offset = this.pos;
        int len = 1;
        final StringBuilder symbol = new StringBuilder();
        AbstractOperator lastValid = null;
        symbol.append(firstChar);

        while (!isEndOfExpression(offset + len)  &&
                AllowedCharsetUtil.isAllowedOperatorChar(expression[offset + len])) {
            symbol.append(expression[offset + len++]);
        }

        while (symbol.length() > 0) {
            AbstractOperator op = this.getOperator(symbol.toString());
            if (op == null) {
                symbol.setLength(symbol.length() - 1);
            }else{
                lastValid = op;
                break;
            }
        }

        pos += symbol.length();
        lastToken = new OperatorExpressionToken(lastValid);
        return lastToken;
    }

    /**
     *
     * @param symbol passed Operator Symbol
     * @return {@link AbstractOperator} or null if this is not registered
     */
    private AbstractOperator getOperator(String symbol) {
        AbstractOperator op = null;
        if (this.userOperators != null) {
            op = this.userOperators.get(symbol);
        }
        if (op == null && symbol.length() == 1) {
            int argc = 2;
            if (lastToken == null) {
                argc = 1;
            } else {
                TokenEnum lastTokenType = lastToken.getType();
                if (lastTokenType == TokenEnum.TOKEN_PARENTHESES_OPEN || lastTokenType == TokenEnum.TOKEN_SEPARATOR) {
                    argc = 1;
                } else if (lastTokenType == TokenEnum.TOKEN_OPERATOR) {
                    final AbstractOperator lastOp = ((OperatorExpressionToken) lastToken).getOperator();
                    if (lastOp.getNumOperands() == 2 || (lastOp.getNumOperands() == 1 && !lastOp.isLeftAssociative())) {
                        argc = 1;
                    }
                }

            }
            op = BuiltInOperators.getBuiltinOperator(symbol.charAt(0), argc);
        }
        return op;
    }

    /**
     *
     * @param firstChar charecter of the expression
     * @return {@link NumberExpressionToken}
     */
    private AbstractExpressionToken parseNumberToken(final char firstChar) {
        final int offset = this.pos;
        int len = 1;
        this.pos++;
        if (isEndOfExpression(offset + len)) {
            lastToken = new NumberExpressionToken(Double.parseDouble(String.valueOf(firstChar)));
            return lastToken;
        }
        while (!isEndOfExpression(offset + len) &&
                isNumeric(expression[offset + len], expression[offset + len - 1] == 'e' ||
                        expression[offset + len - 1] == 'E')) {
            len++;
            this.pos++;
        }
        // check if the e is at the end
        if (expression[offset + len - 1] == 'e' || expression[offset + len - 1] == 'E') {
            // since the e is at the end it's not part of the number and a rollback is necessary
            len--;
            pos--;
        }
        lastToken = new NumberExpressionToken(expression, offset, len);
        return lastToken;
    }

    /**
     *
     * @param ch char of expression
     * @param lastCharE true if last char is E or e
     * @return true or false
     */
    private boolean isNumeric(char ch, boolean lastCharE) {
        return Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E' ||
                (lastCharE && (ch == '-' || ch == '+'));
    }


    /**
     *
     * @param codePoint unicode point
     * @return true if this is letter
     */
    public boolean isAlphabetic(int codePoint) {
        return Character.isLetter(codePoint);
    }

    /**
     *
     * @param offset offset value
     * @return true if expressionLength is less than or equal to offset
     */
    private boolean isEndOfExpression(int offset) {
        return this.expressionLength <= offset;
    }

    /**
     * Builder class
     */
    public static class TokenizerBuilder{
        private char[] expression;
        private Map<String, AbstractFunction> userFunctions;
        private Map<String, AbstractOperator> userOperators;
        private Set<String> variableNames;

        public TokenizerBuilder(String expression) {
            if(expression == null || expression.trim().equals("")){
                throw new IllegalArgumentException("Invalid Expression");
            }
            this.expression = expression.trim().toCharArray();
        }

        public TokenizerBuilder withCustomUserFunctions(Map<String, AbstractFunction> userFunctions){
            this.userFunctions = userFunctions;
            return this;
        }

        public TokenizerBuilder withCustomUserOperators(Map<String, AbstractOperator> userOperators){
            this.userOperators = userOperators;
            return this;
        }

        public TokenizerBuilder withVariableNames(Set<String> variableNames){
            this.variableNames = variableNames;
            return this;
        }

        public ExpressionTokenizer build(){
            return new ExpressionTokenizer(this);
        }

    }
}

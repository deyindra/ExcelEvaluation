package org.idey.excel.expression.tokenizer;

/**
 * @author i.dey
 * Abstract class for tokens used to tokenize expressions
 * @see ArgumentSeparatorExpressionToken
 * @see CloseParenthesesExpressionToken
 * @see FunctionExpressionToken
 * @see OpenParenthesesExpressionToken
 * @see VariableExpressionToken
 * @see OperatorExpressionToken
 * @see NumberExpressionToken
 */
public abstract class AbstractExpressionToken {

    /**
     * Type of AbstractExpressionToken refer {@link TokenEnum}
     */
    protected final TokenEnum type;

    AbstractExpressionToken(TokenEnum type) {
        this.type = type;
    }

    public TokenEnum getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AbstractExpressionToken{" +
                "type=" + type +
                '}';
    }
}

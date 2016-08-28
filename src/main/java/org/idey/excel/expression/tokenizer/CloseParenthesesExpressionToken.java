package org.idey.excel.expression.tokenizer;

/**
 * represents closed parentheses
 * @see AbstractExpressionToken
 */
public final class CloseParenthesesExpressionToken extends AbstractExpressionToken {

    /**
     * Creare a new instance
     */
    CloseParenthesesExpressionToken() {
        super(TokenEnum.TOKEN_PARENTHESES_CLOSE);
    }

    @Override
    public String toString() {
        return "CloseParenthesesExpressionToken{} " + super.toString();
    }
}

package org.idey.excel.expression.tokenizer;

/**
 * @author i.dey
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

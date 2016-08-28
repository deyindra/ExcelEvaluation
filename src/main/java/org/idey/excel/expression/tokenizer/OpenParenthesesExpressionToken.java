package org.idey.excel.expression.tokenizer;

/**
 * represents open parentheses
 * @see AbstractExpressionToken
 */
public final class OpenParenthesesExpressionToken extends AbstractExpressionToken {

    OpenParenthesesExpressionToken() {
        super(TokenEnum.TOKEN_PARENTHESES_OPEN);
    }

    @Override
    public String toString() {
        return "OpenParenthesesExpressionToken{} " + super.toString();
    }
}

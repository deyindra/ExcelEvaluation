package org.idey.excel.expression.tokenizer;

/**
 * @author i.dey
 * Represents an argument separator in functions i.e: ','
 * @see AbstractExpressionToken
 */
public final class ArgumentSeparatorExpressionToken extends AbstractExpressionToken {
    /**
     * Create a new instance
     */
    ArgumentSeparatorExpressionToken() {
        super(TokenEnum.TOKEN_SEPARATOR);
    }

    @Override
    public String toString() {
        return "ArgumentSeparatorExpressionToken{} " + super.toString();
    }
}

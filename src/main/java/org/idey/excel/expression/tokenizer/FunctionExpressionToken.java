package org.idey.excel.expression.tokenizer;

import org.idey.excel.expression.function.AbstractFunction;

/**
 * @author i.dey
 * represents function
 * @see AbstractExpressionToken
 */
public final class FunctionExpressionToken extends AbstractExpressionToken {
    private final AbstractFunction function;
    FunctionExpressionToken(final AbstractFunction function) {
        super(TokenEnum.TOKEN_FUNCTION);
        if (function == null) {
            throw new IllegalArgumentException("function is unknown for token.");
        }
        this.function = function;
    }

    public AbstractFunction getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return "FunctionExpressionToken{" +
                "function=" + function +
                "} " + super.toString();
    }
}

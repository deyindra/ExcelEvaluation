package org.idey.excel.expression.tokenizer;

/**
 * Represents a number in the expression
 * @see AbstractExpressionToken
 */
public final class NumberExpressionToken extends AbstractExpressionToken {
    private final double value;

    /**
     * Create a new instance
     * @param value the value of the number
     */
    NumberExpressionToken(double value) {
        super(TokenEnum.TOKEN_NUMBER);
        this.value = value;
    }

    NumberExpressionToken(final char[] expression, final int offset, final int len) {
            this(Double.parseDouble(String.valueOf(expression, offset, len)));
    }

    /**
     * Get the value of the number
     * @return the value
     */
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NumberExpressionToken{" +
                "value=" + value +
                "} " + super.toString();
    }
}

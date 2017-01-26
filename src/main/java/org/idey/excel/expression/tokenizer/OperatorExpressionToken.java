package org.idey.excel.expression.tokenizer;

import org.idey.excel.expression.operator.AbstractOperator;

/**
 * @author i.dey
 * Represents an operator used in expressions
 * @see AbstractExpressionToken
 */
public final class OperatorExpressionToken extends AbstractExpressionToken {
    private final AbstractOperator operator;

    /**
     * Create a new instance
     * @param op the operator
     */
    public OperatorExpressionToken(AbstractOperator op) {
        super(TokenEnum.TOKEN_OPERATOR);
        if (op == null) {
            throw new IllegalArgumentException("Operator is unknown for token.");
        }
        this.operator = op;
    }

    /**
     * Get the operator for that token
     * @return the operator
     */
    public AbstractOperator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "OperatorExpressionToken{" +
                "operator=" + operator +
                "} " + super.toString();
    }
}

package org.idey.excel.expression.tokenizer;

/**
 * @author i.dey
 * Type of AbstractExpressionToken parsing from expression
 */
public enum TokenEnum {
    /**
     * Number AbstractExpressionToken
     */
    TOKEN_NUMBER,
    /**
     * Operator AbstractExpressionToken
     */
    TOKEN_OPERATOR,
    /**
     * Function token
     */
    TOKEN_FUNCTION,
    /**
     * Open parentheses token either '{', '(' or '['
     */
    TOKEN_PARENTHESES_OPEN,
    /**
     * Close parentheses token either '}', ')' or ']'
     */
    TOKEN_PARENTHESES_CLOSE,
    /**
     * Variable token
     */
    TOKEN_VARIABLE,
    /**
     * Separator token which is separate agruments in function by ','
     */
    TOKEN_SEPARATOR
}

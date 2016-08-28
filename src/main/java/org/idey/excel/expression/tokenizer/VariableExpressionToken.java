package org.idey.excel.expression.tokenizer;

/**
 * represents a setVariable used in an expression
 * @see AbstractExpressionToken
 */
public final class VariableExpressionToken extends AbstractExpressionToken {
    private final String name;

    /**
     * Get the name of the setVariable
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new instance
     * @param name the name of the setVariable
     */
    VariableExpressionToken(String name) {
        super(TokenEnum.TOKEN_VARIABLE);
        if(name==null || name.trim().equals("")){
            throw new IllegalArgumentException("Invalid variable name");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return "VariableExpressionToken{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}

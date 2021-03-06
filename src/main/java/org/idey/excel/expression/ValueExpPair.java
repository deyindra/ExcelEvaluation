package org.idey.excel.expression;


/**
 * @author i.dey
 * This class used substitue a variable by {@link Double} or by {@link MathExpression}
 */
final class ValueExpPair {
    private Double value;
    private MathExpression expression;
    private boolean isExpression;

    public ValueExpPair(Double value) {
        if(value==null){
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
        isExpression = false;
    }

    public ValueExpPair(MathExpression expression) {
        if(expression == null){
            throw new IllegalArgumentException("Invalid Expression");
        }
        this.expression = expression;
        isExpression = true;
    }

    public Double getValue() {
        return value;
    }

    public MathExpression getExpression() {
        return expression;
    }

    public boolean isExpression() {
        return isExpression;
    }
}

package org.idey.excel.expression.function;

public abstract class AbstractFunctionEvaluateTest {
    protected AbstractFunction function;
    protected Double[] args;

    public AbstractFunctionEvaluateTest(AbstractFunction function, Double[] args) {
        this.function = function;
        this.args = args;
    }
}

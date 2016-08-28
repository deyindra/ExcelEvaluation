package org.idey.excel.expression.operator;

public abstract class AbstractOperatorEvaluateTest {
    protected AbstractOperator operator;
    protected Double[] array;

    protected AbstractOperatorEvaluateTest(AbstractOperator operator,
                                           Double[] array) {
        this.operator = operator;
        this.array = array;
    }
}

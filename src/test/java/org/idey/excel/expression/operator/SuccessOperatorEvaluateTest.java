package org.idey.excel.expression.operator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessOperatorEvaluateTest extends AbstractOperatorEvaluateTest{
    private Double expectedOutput;

    public SuccessOperatorEvaluateTest(AbstractOperator operator,
                                       Double[] array, Double expectedOutput) {
        super(operator, array);
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {BuiltInOperators.UNARYPLUS, new Double[]{1d}, 1d},
                {BuiltInOperators.UNARYMINUS, new Double[]{1d}, -1d},
                {BuiltInOperators.ADDITION, new Double[]{1d,2d}, 3d},
                {BuiltInOperators.SUBTRACTION, new Double[]{1d,2d}, -1d},
                {BuiltInOperators.MUTLIPLICATION, new Double[]{2d,2d}, 4d},
                {BuiltInOperators.DIVISION, new Double[]{6d,3d}, 2d},
                {BuiltInOperators.POWER, new Double[]{4d,2d}, 16d},
                {BuiltInOperators.MODULO, new Double[]{4d,3d}, 1d},
        });
    }

    @Test
    public void test(){
        Assert.assertEquals(expectedOutput, operator.checkAndApply(array));
    }

}

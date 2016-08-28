package org.idey.excel.expression.function;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessFunctionEvaluateTest extends AbstractFunctionEvaluateTest{
    private Double expectedResult;
    public SuccessFunctionEvaluateTest(AbstractFunction function, Double[] args,
                                       Double expectedResult) {
        super(function, args);
        this.expectedResult = expectedResult;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {BuiltInFunctions.SIN, new Double[]{0d}, 0d},
            {BuiltInFunctions.COS, new Double[]{0d}, 1d},
            {BuiltInFunctions.TAN, new Double[]{0d}, 0d},
            {BuiltInFunctions.LOG, new Double[]{0d}, Double.NEGATIVE_INFINITY},
            {BuiltInFunctions.LOG2, new Double[]{0d}, Double.NEGATIVE_INFINITY},
            {BuiltInFunctions.LOG10, new Double[]{0d}, Double.NEGATIVE_INFINITY},
            {BuiltInFunctions.LOG1P, new Double[]{0d}, 0d},
            {BuiltInFunctions.ABS, new Double[]{-4d}, 4d},
            {BuiltInFunctions.ACOS, new Double[]{0d}, 1.5707963267948966d},
            {BuiltInFunctions.ASIN, new Double[]{0d}, 0d},
            {BuiltInFunctions.ATAN, new Double[]{0d}, 0d},
            {BuiltInFunctions.CBRT, new Double[]{0d}, 0d},
            {BuiltInFunctions.FLOOR, new Double[]{1.1d}, 1d},
            {BuiltInFunctions.SINH, new Double[]{1d}, 1.1752011936438014d},
            {BuiltInFunctions.SQRT, new Double[]{4d}, 2d},
            {BuiltInFunctions.TANH, new Double[]{1d}, 0.7615941559557649d},
            {BuiltInFunctions.COSH, new Double[]{1d}, 1.543080634815244d},
            {BuiltInFunctions.CEIL, new Double[]{1.1d}, 2d},
            {BuiltInFunctions.POWER, new Double[]{2d, 3d}, 8d},
            {BuiltInFunctions.EXP, new Double[]{1d}, 2.718281828459045d},
            {BuiltInFunctions.EXPM1, new Double[]{1d}, 1.718281828459045d},
            {BuiltInFunctions.SIGNUM, new Double[]{4d}, 1d},
            {BuiltInFunctions.SIGNUM, new Double[]{-4d}, -1d},
            {BuiltInFunctions.SIGNUM, new Double[]{0d}, 0d},
        });
    }

    @Test
    public void test(){
        Assert.assertEquals(expectedResult,function.checkAndApply(args));
    }
}

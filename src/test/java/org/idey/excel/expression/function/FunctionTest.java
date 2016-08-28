package org.idey.excel.expression.function;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class FunctionTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "checkValidFunction")
    public void test(String functionName, int numArgument, Double... args){
        AbstractFunction function = new AbstractFunction(functionName,numArgument) {
            @Override
            protected Double apply(Double... args) {
                return (double)args.length;
            }
        };
        Assert.assertEquals(functionName, function.getName());
        Assert.assertEquals(numArgument, function.getNumArguments());
        Assert.assertTrue((double)args.length==function.checkAndApply(args));
    }

    private Object[] checkValidFunction() {
        return new Object[]{
                new Object[]{"xyz",3,new Double[]{1d,2d,3d}},
                new Object[]{"abc",2,new Double[]{1d,2d}},
                new Object[]{"abc10_",1, new Double[]{1d}},
                new Object[]{"abc10",0, new Double[]{}},
        };
    }


    @Test
    @Parameters(method = "checkFailedFunction")
    public void test(String functionName, int numArgument){
        expectedException.expect(Exception.class);
        AbstractFunction function = new AbstractFunction(functionName,numArgument) {
            @Override
            protected Double apply(Double... args) {
                return (double)args.length;
            }
        };
    }

    private Object[] checkFailedFunction() {
        return new Object[]{
                new Object[]{"",3},
                new Object[]{" ",2},
                new Object[]{"abc10_",-1},
                new Object[]{null,1},
                new Object[]{"#$%",1},
                new Object[]{"12A",1},

        };
    }
}

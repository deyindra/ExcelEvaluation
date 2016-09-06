package org.idey.excel.expression.operator;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class FailedBuiltInOperatorWithZeroTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Test
    @Parameters(method = "checkFailedTest")
    public void test(AbstractOperator operator,Double[] args){
        expectedException.expect(ArithmeticException.class);
        operator.checkAndApply(args);
    }

    private Object[] checkFailedTest() {
        return new Object[]{
                new Object[]{BuiltInOperators.DIVISION, new Double[]{1d,0d}},
                new Object[]{BuiltInOperators.MODULO, new Double[]{1d,0d}},
        };
    }




}

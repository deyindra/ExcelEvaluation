package org.idey.excel.expression.function;

import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FailureFunctionEvaluateTest
        extends AbstractFunctionEvaluateTest{
    public FailureFunctionEvaluateTest(AbstractFunction function, Double[] args) {
        super(function, args);
    }

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {BuiltInFunctions.SIN, new Double[]{1d,2d}},
                {BuiltInFunctions.COS, new Double[]{1d,2d}},
                {BuiltInFunctions.TAN, null},
        });
    }

    @Test
    public void test(){
        expectedException.expect(IllegalArgumentException.class);
        function.checkAndApply(args);
    }
}

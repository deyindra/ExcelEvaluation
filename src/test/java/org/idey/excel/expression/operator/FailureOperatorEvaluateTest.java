package org.idey.excel.expression.operator;

import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FailureOperatorEvaluateTest extends AbstractOperatorEvaluateTest{

    public FailureOperatorEvaluateTest(AbstractOperator operator, Double[] array) {
        super(operator, array);
    }

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {BuiltInOperators.UNARYPLUS, new Double[]{1d,2d}},
                {BuiltInOperators.UNARYMINUS, new Double[]{1d,2d}},
                {BuiltInOperators.ADDITION, new Double[]{1d}},
                {BuiltInOperators.SUBTRACTION, null},
                {BuiltInOperators.MUTLIPLICATION, new Double[]{}},
        });
    }

    @Test
    public void test(){
        expectedException.expect(IllegalArgumentException.class);
        operator.checkAndApply(array);
    }

}

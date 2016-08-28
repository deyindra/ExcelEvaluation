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
public class FailureBuiltInFunctionTest extends AbstractBuiltInFunctionTest{
    public FailureBuiltInFunctionTest(String functionName) {
        super(functionName);
    }

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {" "},
                {null}
        });
    }

    @Test
    public void test(){
        expectedException.expect(IllegalArgumentException.class);
        BuiltInFunctions.getBuiltinFunction(functionName);
    }
}

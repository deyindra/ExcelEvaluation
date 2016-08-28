package org.idey.excel.expression.tokenizer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.expression.function.BuiltInFunctions;
import org.idey.excel.expression.operator.BuiltInOperators;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;


@RunWith(JUnitParamsRunner.class)
public class FailureTokenTest{

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void functionTokenTest(){
        expectedException.expect(IllegalArgumentException.class);
        new FunctionExpressionToken(BuiltInFunctions.getBuiltinFunction("ABC"));
    }

    @Test
    public void operatorTokenTest(){
        expectedException.expect(IllegalArgumentException.class);
        new OperatorExpressionToken(BuiltInOperators.getBuiltinOperator('^',4));
    }

    @Test
    @Parameters(method = "checkNumberToken")
    public void numberTokenTest(char[] array, int offset, int length){
        expectedException.expect(Exception.class);
        new NumberExpressionToken(array,offset,length);
    }


    private Object[] checkNumberToken() {
        return new Object[]{
                new Object[]{"ABCXYZ".toCharArray(),3,3},
                new Object[]{new char[]{},3,3},
                new Object[]{"ABCXYZ".toCharArray(),3,4},
                new Object[]{null,3,3},
                new Object[]{"ABC123".toCharArray(),3,4},
                new Object[]{"ABC123".toCharArray(),3,-4},
                new Object[]{"ABC123".toCharArray(),-3,4},
        };
    }

    @Test
    @Parameters(method = "checkVariableToken")
    public void variableTokenTest(String name){
        expectedException.expect(Exception.class);
        new VariableExpressionToken(name);
    }

    private Object[] checkVariableToken() {
        return new Object[]{
                new Object[]{null},
                new Object[]{" "}
        };
    }

}

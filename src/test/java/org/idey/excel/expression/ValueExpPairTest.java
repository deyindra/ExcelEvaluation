package org.idey.excel.expression;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ValueExpPairTest {

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "successTest")
    public void successTest(ValueExpPair valueExpPair,Boolean isExpression){
        Assert.assertEquals(valueExpPair.isExpression(),isExpression);
    }


    private Object[] successTest() {
        return new Object[]{
            new Object[]{new ValueExpPair(6d), false},
            new Object[]{new ValueExpPair(new MathExpression.MathExpressionBuilder("2+3").build()), true}
        };
    }


    @Test
    @Parameters(method = "failureTest")
    public void FailureTest(Object value,Boolean isExpression){
        expectedException.expect(Exception.class);
        if(!isExpression){
            ValueExpPair valueExpPair = new ValueExpPair((Double)value);
        }else{
            ValueExpPair valueExpPair = new ValueExpPair((MathExpression)value);
        }
    }


    private Object[] failureTest() {
        return new Object[]{
                new Object[]{null, false},
                new Object[]{null, true}
        };
    }

}

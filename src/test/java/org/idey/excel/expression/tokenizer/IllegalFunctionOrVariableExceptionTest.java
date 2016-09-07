package org.idey.excel.expression.tokenizer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JUnitParamsRunner.class)
public class IllegalFunctionOrVariableExceptionTest {

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @Test
    @Parameters(method = "testExpression")
    public void test(String expession, int postion, int length, String token){
        IllegalFunctionOrVariableException exception = new IllegalFunctionOrVariableException(expession,postion,length);
        Assert.assertEquals(expession,exception.getExpression());
        Assert.assertEquals(postion,exception.getPosition());
        Assert.assertEquals(token,exception.getToken());
        Assert.assertEquals(exception.getMessage(),String.format(IllegalFunctionOrVariableException.ERROR_MESSAGE_FORMAT,
                exception.getToken(),exception.getPosition(),exception.getExpression()));
    }

    private Object[] testExpression() {
        return new Object[]{
                new Object[]{"2x3",1,"x".length(),"x"},
                new Object[]{"2+3x",3,2,"x"},
        };
    }

}

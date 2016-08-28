package org.idey.excel.expression.function;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessBuiltInFunctionTest extends AbstractBuiltInFunctionTest{
    private AbstractFunction expextedResult;

    public SuccessBuiltInFunctionTest(String functionName,
                                      AbstractFunction expextedResult) {
        super(functionName);
        this.expextedResult = expextedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sin", BuiltInFunctions.SIN},
                {" cos ", BuiltInFunctions.COS},
                {"TAN", BuiltInFunctions.TAN},
                {"XYZ", null}
        });
    }

    @Test
    public void test(){
        if(expextedResult==null){
            Assert.assertNull(BuiltInFunctions.getBuiltinFunction(functionName));
        }else{
            Assert.assertEquals(expextedResult,BuiltInFunctions.getBuiltinFunction(functionName));
        }
    }
}

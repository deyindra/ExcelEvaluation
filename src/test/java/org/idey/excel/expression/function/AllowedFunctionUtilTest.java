package org.idey.excel.expression.function;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AllowedFunctionUtilTest {
    private String name;
    private boolean expectedResult;

    public AllowedFunctionUtilTest(String name, boolean expectedResult) {
        this.name = name;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"ABC", true},
                {"ABC10", true},
                {null, false},
                {" ", false},
                {"0ABC", false},
                {" ABC ", true},
                {" ABC_ ", true},
                {" ^ABC_ ", false},
        });
    }
    @Test
    public void test(){
        Assert.assertEquals(expectedResult,AllowedFunctionUtil.isValidFunctionName(name));
    }
}

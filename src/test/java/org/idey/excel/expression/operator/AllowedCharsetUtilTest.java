package org.idey.excel.expression.operator;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AllowedCharsetUtilTest {
    private char ch;
    private boolean expectedResult;

    public AllowedCharsetUtilTest(char ch, boolean expectedResult) {
        this.ch = ch;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {'+', true},
                {'-', true},
                {'§', true},
                {'1', false},
                {'\0', false}
        });
    }

    @Test
    public void allowedCharTest(){
        Assert.assertEquals(expectedResult, AllowedCharsetUtil.isAllowedOperatorChar(ch));
    }
}

package org.idey.excel;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class PositiveBaseConverterEnumTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "encodingDecodingTest")
    public void testSuccessEncodingDecoding(PositiveBaseConverterEnum positiveBaseConverterEnum,
                                            int input, int expectedOputput) {
        Assert.assertTrue(positiveBaseConverterEnum.decode(positiveBaseConverterEnum.encode(input))
                == expectedOputput);
    }
    private Object[] encodingDecodingTest() {
        return new Object[]{
                new Object[]{PositiveBaseConverterEnum.EXCEL_ENCODING,1,1},
                new Object[]{PositiveBaseConverterEnum.EXCEL_ENCODING,52,52},
                new Object[]{PositiveBaseConverterEnum.EXCEL_ENCODING,126,126},
                new Object[]{PositiveBaseConverterEnum.BINARY,126,126},
                new Object[]{PositiveBaseConverterEnum.BINARY,12,12},
                new Object[]{PositiveBaseConverterEnum.HEX,52,52},
                new Object[]{PositiveBaseConverterEnum.HEX,52,52},
        };
    }

    @Test
    @Parameters(method = "encodingTest")
    public void testFailedEncodingDecoding(PositiveBaseConverterEnum positiveBaseConverterEnum,
                                            int input) {
        expectedException.expect(IllegalArgumentException.class);
        positiveBaseConverterEnum.encode(input);
    }
    private Object[] encodingTest() {
        return new Object[]{
                new Object[]{PositiveBaseConverterEnum.EXCEL_ENCODING,-1},
                new Object[]{PositiveBaseConverterEnum.EXCEL_ENCODING,0},
                new Object[]{PositiveBaseConverterEnum.BINARY,-1},
                new Object[]{PositiveBaseConverterEnum.BINARY,0},
                new Object[]{PositiveBaseConverterEnum.HEX,-1},
                new Object[]{PositiveBaseConverterEnum.HEX,0},
        };
    }

}

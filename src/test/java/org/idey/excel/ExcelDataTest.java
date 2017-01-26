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
public class ExcelDataTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Test
    @Parameters(method = "successExcelData")
    public void testSuccessExcelData(ExcelData object, boolean expectedErrorOutput, String expecectedStringOutput){
        Assert.assertEquals(object.isError(),expectedErrorOutput);
        Assert.assertEquals(object.toString(),expecectedStringOutput);

    }

    private Object[] successExcelData() {
        return new Object[]{
                new Object[]{new ExcelData(1.d),false,"1.0"},
                new Object[]{new ExcelData("Error"),true,"Error"},
                new Object[]{new ExcelData(" Error "),true,"Error"},

        };
    }

    @Test
    @Parameters(method = "failureStringExcelData")
    public void testFailureStringExcelData(String input){
        expectedException.expect(IllegalArgumentException.class);
        new ExcelData(input);
    }

    private Object[] failureStringExcelData() {
        return new Object[]{
                new Object[]{null},
                new Object[]{""},
                new Object[]{" "}
        };
    }

    @Test
    @Parameters(method = "failureDoubleExcelData")
    public void testFailureDoubleExcelData(Double input){
        expectedException.expect(IllegalArgumentException.class);
        new ExcelData(input);
    }

    private Object[] failureDoubleExcelData() {
        return new Object[]{
                new Object[]{null},
                new Object[]{Double.NaN},
        };
    }

}
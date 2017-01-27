package org.idey.excel;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(JUnitParamsRunner.class)
public class ExcelTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelTest.class);
    @Test
    @Parameters(method = "successExcelObjectCreation")
    public void testSuccessExcelObjectCreation(int row, int col, AbstractFunction[] udf){
        Excel excel;
        if(udf!=null && udf.length>0){
            excel = new Excel.ExcelBuilder(row,col,udf).build();
        }else{
            excel = new Excel.ExcelBuilder(row,col).build();
        }
        Assert.assertNotNull(excel);
    }

    private AbstractFunction[] returnAbstractFunctions(){
        return new AbstractFunction[]{
                null,
                new AbstractFunction("now", 0){
                    @Override
                    protected Double apply(Double... args) {
                        return 1d;
                    }
                }
        };
    }

    private Object[] successExcelObjectCreation() {
        return new Object[]{
                new Object[]{2,3,null},
                new Object[]{2,3,new AbstractFunction[]{}},
                new Object[]{2,3,returnAbstractFunctions()}

        };
    }


    @Test
    @Parameters(method = "failureExcelObjectCreation")
    public void testFailureExcelObjectCreation(int row, int col){
        expectedException.expect(IllegalArgumentException.class);
        new Excel.ExcelBuilder(row,col);
    }

    private Object[] failureExcelObjectCreation() {
        return new Object[]{
                new Object[]{0,3},
                new Object[]{3,0},
                new Object[]{-2,3},
                new Object[]{2,-3},
        };
    }

    @Test
    @Parameters(method = "successAddExpression")
    public void testSuccessAddExpression(int totalRows, int totalCols, String expression,int row, int col, String[] dependencyCellName,
                                         ExcelData expectedExcelData, AbstractFunction[] udf){
        Excel excel = getExcel(totalRows,totalCols,udf);
        String cellName = String.format(Excel.CELL_NAME,
                PositiveBaseConverterEnum.EXCEL_ENCODING.encode(col),row).toLowerCase();
        excel.addExpression(expression,cellName,dependencyCellName);
        ExcelData[][] array = excel.evaluateData();
        Assert.assertEquals(array[row-1][col-1],expectedExcelData);
    }

    private Object[] successAddExpression() {
        return new Object[]{
                new Object[]{2,3,"2+3",1,1, null, new ExcelData(5d), null},
                new Object[]{2,3,"2+3",1,1, new String[]{}, new ExcelData(5d), null},
                new Object[]{2,3,"2+A2", 1,1, new String[]{"A2"}, new ExcelData(2d), new AbstractFunction[]{}},
                new Object[]{2,3,"2+A2", 1,1, new String[]{"A2"}, new ExcelData(2d),returnAbstractFunctions()},
                new Object[]{2,3,"2+A2", 1,1, new String[]{"A2"}, new ExcelData(2d),new AbstractFunction[]{null}},
                new Object[]{2,3,"2+A2", 1,1, new String[]{"A2"}, new ExcelData(2d),returnAbstractFunctions()},

        };
    }

    @Test
    @Parameters(method = "failureAddExpression")
    public void testFailureAddExpression(int totalRows, int totalCols, String expression,int row, int col,
                                         String[] dependencyCellName, AbstractFunction[] udf){
        expectedException.expect(IllegalArgumentException.class);
        Excel excel = getExcel(totalRows,totalCols, udf);

        String cellName = null;
        if(row>0 && col>0) {
            cellName = String.format(Excel.CELL_NAME,
                    PositiveBaseConverterEnum.EXCEL_ENCODING.encode(col), row).toLowerCase();
        }else if(row==-2 && col==-2){
            cellName ="";
        }else if(row==-3 && col==-3){
            cellName =" ";
        }
        excel.addExpression(expression,cellName,dependencyCellName);
    }

    private Object[] failureAddExpression() {
        return new Object[]{
                new Object[]{2,3,"",1,1, null, null},
                new Object[]{2,3," ", 1,1, new String[]{"A2"}, null},
                new Object[]{2,3,null, 1,1, new String[]{"A2"}, new AbstractFunction[]{}},
                new Object[]{2,3,"2+3", 5,6, new String[]{"A2"},returnAbstractFunctions()},
                new Object[]{2,3,"2+3", -1,-1, new String[]{"A2"}, null},
                new Object[]{2,3,"2+3", -2,-2, new String[]{"A2"}, null},
                new Object[]{2,3,"2+3", -3,-3, new String[]{"A2"}, null},
                new Object[]{2,3,"2+3", 1,1, new String[]{null}, null},
                new Object[]{2,3,"2+3", 1,1, new String[]{"A12"}, null},
                new Object[]{2,3,"2+3", 1,1, new String[]{""}, new AbstractFunction[]{null}},
                new Object[]{2,3,"2+3", 1,1, new String[]{" "}, returnAbstractFunctions()}
        };
    }

    private Excel getExcel(int rows, int cols, AbstractFunction[] udf){
        return new Excel.ExcelBuilder(rows,cols,udf).build();
    }

    @Test
    public void evaluationTest(){
        Excel excel = new Excel.ExcelBuilder(2,3, returnAbstractFunctions()).build();
        excel.addExpression("now","A1");
        ExcelData[][] array = excel.evaluateData();
        Assert.assertEquals(array[0][0], new ExcelData(1d));
        excel.clear();
        array = excel.evaluateData();
        Assert.assertEquals(array[0][0], new ExcelData(0d));
        excel.clear();
        excel.addExpression("now","A1");
        excel.addExpression("A1+1","B1", "A1");
        array = excel.evaluateData();
        Assert.assertEquals(array[0][0], new ExcelData(1d));
        Assert.assertEquals(array[0][1], new ExcelData(2d));
        excel.clear();
        excel.addExpression("now","A1");
        excel.addExpression("(A1+1)/C1","B1", "A1", "C1");
        excel.addExpression("B1+1","B2", "B1");
        array = excel.evaluateData();
        Assert.assertEquals(array[0][0], new ExcelData(1d));
        Assert.assertTrue(array[0][1].isError());
        Assert.assertTrue(array[1][1].isError());
        excel.clear();
    }


}
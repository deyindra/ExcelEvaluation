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
public class ExpressionValidationResultTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Test
    @Parameters(method = "checkValidValidationResultWithArray")
    public void successValidationResultTestWithArray(ExpressionValidationResult result, String[] errors){
        if(!result.isSuccess()){
            result.addError(errors);
            Assert.assertTrue(!result.getErrors().isEmpty());
        }else{
            Assert.assertTrue(result.getErrors().isEmpty());
        }
    }

    private Object[] checkValidValidationResultWithArray() {
        return new Object[]{
                new Object[]{ExpressionValidationResult.SUCCESS,null},
                new Object[]{new ExpressionValidationResult(),new String[]{"ERROR1, ERROR2"}}
        };
    }


    @Test
    @Parameters(method = "checkValidValidationResult")
    public void successValidationResultTest(ExpressionValidationResult result, String error){
        if(!result.isSuccess()){
            result.addError(error);
            Assert.assertTrue(!result.getErrors().isEmpty());
        }else{
            Assert.assertTrue(result.getErrors().isEmpty());
        }
    }

    private Object[] checkValidValidationResult() {
        return new Object[]{
                new Object[]{ExpressionValidationResult.SUCCESS,null},
                new Object[]{new ExpressionValidationResult(),"ERROR1"}
        };
    }


    @Test
    @Parameters(method = "checkFailedValidValidationResult")
    public void failureValidationResult(ExpressionValidationResult result, String error){
        expectedException.expect(Exception.class);
        result.addError(error);
    }

    private Object[] checkFailedValidValidationResult() {
        return new Object[]{
                new Object[]{ExpressionValidationResult.SUCCESS,null},
                new Object[]{ExpressionValidationResult.SUCCESS,"ERROR1"},
                new Object[]{new ExpressionValidationResult(),null},
                new Object[]{new ExpressionValidationResult()," "},
                new Object[]{new ExpressionValidationResult(),""},

        };
    }


    @Test
    @Parameters(method = "checkFailedValidValidationResultWithArray")
    public void failureValidationResultWithArray(ExpressionValidationResult result, String[] error){
        expectedException.expect(Exception.class);
        result.addError(error);
    }

    private Object[] checkFailedValidValidationResultWithArray() {
        return new Object[]{
                new Object[]{ExpressionValidationResult.SUCCESS,null},
                new Object[]{ExpressionValidationResult.SUCCESS,new String[]{"ERROR"}},
                new Object[]{new ExpressionValidationResult(),null},
                new Object[]{new ExpressionValidationResult(),new String[]{}},
                new Object[]{new ExpressionValidationResult(),new String[]{null}},
                new Object[]{new ExpressionValidationResult(),new String[]{""}},
        };
    }

}

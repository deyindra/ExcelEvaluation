package org.idey.excel.expression.tokenizer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.expression.OperatorAndFunctionUtil;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@RunWith(JUnitParamsRunner.class)
public class ExpressionTokenizerTest extends OperatorAndFunctionUtil{

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Test
    @Parameters(method = "checkNumberToken")
    public void test(String expression, int numberOfExpctedToken){
        ExpressionTokenizer expressionTokenizer =
                new ExpressionTokenizer.TokenizerBuilder(expression)
                        .withCustomUserOperators(operatorMap)
                        .withCustomUserFunctions(functionMap)
                        .withVariableNames(new HashSet<>(Arrays.asList("e","pi","π","φ")))
                        .build();
        int count=0;
        while (expressionTokenizer.hasNext()){
            LOGGER.info(expressionTokenizer.next().toString());
            count++;
        }
        Assert.assertEquals(numberOfExpctedToken,count);
    }

    private Object[] checkNumberToken() {
        Object[] objects = new Object[list.size()];
        for(int count=0;count<list.size();count++){
            Expression expression = list.get(count);
            objects[count] = new Object[]{expression.getExpression(),
                    expression.getExpectedOutputOfExpressionTest()};
        }
        return objects;
    }


    @Test
    @Parameters(method = "checkNumberTokenWithVariables")
    public void testWithVariables(String expression, Set<String> variables, int numberOfExpctedToken){
        ExpressionTokenizer expressionTokenizer = new ExpressionTokenizer.TokenizerBuilder(expression)
                .withVariableNames(variables).build();
        int count=0;
        while (expressionTokenizer.hasNext()){
            LOGGER.info(expressionTokenizer.next().toString());
            count++;
        }
        Assert.assertEquals(numberOfExpctedToken,count);
    }

    private Object[] checkNumberTokenWithVariables() {
        return new Object[]{
                new Object[]{"2x",new HashSet<>(Arrays.asList("x")),3},
                new Object[]{"2(x)(y)",new HashSet<>(Arrays.asList("x", "y")),9},

        };
    }


    @Test
    @Parameters(method = "checkNumberTokenWithOperators")
    public void testWithOperators(String expression, int numberOfExpctedToken){
        ExpressionTokenizer expressionTokenizer = new ExpressionTokenizer.TokenizerBuilder(expression)
                .withCustomUserOperators(operatorMap).build();
        int count=0;
        while (expressionTokenizer.hasNext()){
            LOGGER.info(expressionTokenizer.next().toString());
            count++;
        }
        Assert.assertEquals(numberOfExpctedToken,count);
    }

    private Object[] checkNumberTokenWithOperators() {
        return new Object[]{
                new Object[]{"2!",2},
        };
    }



    @Test
    @Parameters(method = "checkNumberTokenWithFunctions")
    public void testWithFunctions(String expression, int numberOfExpctedToken){
        ExpressionTokenizer expressionTokenizer = new ExpressionTokenizer.TokenizerBuilder(expression)
                .withCustomUserFunctions(functionMap).build();
        int count=0;
        while (expressionTokenizer.hasNext()){
            LOGGER.info(expressionTokenizer.next().toString());
            count++;
        }
        Assert.assertEquals(numberOfExpctedToken,count);
    }

    private Object[] checkNumberTokenWithFunctions() {
        return new Object[]{
                new Object[]{"now()",3},
        };
    }

    @Test
    @Parameters(method = "checkTestWithFailure")
    public void failedtest(String expression){
        expectedException.expect(Exception.class);
        ExpressionTokenizer expressionTokenizer = new ExpressionTokenizer.TokenizerBuilder(expression).build();
        while (expressionTokenizer.hasNext()){
            LOGGER.info(expressionTokenizer.next().toString());
        }
    }


    private Object[] checkTestWithFailure() {
        return new Object[]{
                new Object[]{null},
                new Object[]{" "},
                new Object[]{"8 8"},
                new Object[]{"3.."},
                new Object[]{"?"},
                new Object[]{"ABC()"},
                new Object[]{"2+x"},

        };
    }

}

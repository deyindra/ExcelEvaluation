package org.idey.excel.expression;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.expression.operator.AbstractOperator;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class MathExpressionTest extends OperatorAndFunctionUtil{
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "checkExpression")
    public void testExpression(String strExpression){
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                .build();
        Assert.assertTrue(expression.validate().isSuccess());
        LOGGER.info(String.format("%s = %f",strExpression, expression.evaluate()));
    }
    private Object[] checkExpression() {
        Object[] objects = new Object[list.size()];
        for(int count=0;count<list.size();count++){
            Expression expression = list.get(count);
            objects[count] = new Object[]{expression.getExpression()};
        }
        return objects;
    }


    @Test
    @Parameters(method = "checkExpressionWithVariable")
    public void testExpressionWithVariable(String strExpression, Object[][] array){
        MathExpression.MathExpressionBuilder builder = new MathExpression.MathExpressionBuilder(strExpression)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]));

        for(Object[] arr:array){
            builder.withVariableOrExpressionsNames((String)arr[0]);
        }
        MathExpression expression = builder.build();
        for(Object[] arr:array){
            expression.setValue((String)arr[0], (Double)arr[1]);
        }
        Assert.assertTrue(expression.validate().isSuccess());
        LOGGER.info(String.format("%s = %f",strExpression, expression.evaluate()));
    }

    private Object[] checkExpressionWithVariable() {
        return new Object[]{
            new Object[]{"x+y", new Object[][]{{"x",2d},{"y",2d}}},
            new Object[]{"-x", new Object[][]{{"x",2d}}},
            new Object[]{"x!y!x", new Object[][]{{"x",2d},{"y",2d}}},
        };
    }

    @Test
    @Parameters(method = "checkExpressionWithExpression")
    public void testExpressionWithExpression(String strExpression, String[][] array){
        MathExpression.MathExpressionBuilder builder = new MathExpression.MathExpressionBuilder(strExpression)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]));

        for(String[] arr:array){
            builder.withVariableOrExpressionsNames(arr[0]);
        }
        MathExpression expression = builder.build();
        for(String[] arr:array){
            expression.setExpression(arr[0], new MathExpression.MathExpressionBuilder(arr[1])
                    .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                    .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                    .build()
            );
        }
        Assert.assertTrue(expression.validate().isSuccess());
        LOGGER.info(String.format("%s = %f",strExpression, expression.evaluate()));
    }

    private Object[] checkExpressionWithExpression() {
        return new Object[]{
                new Object[]{"x+y", new String[][]{{"X","2+3"},{"y","3+1"}}},
                new Object[]{"-x", new String[][]{{"x","-3"}}},
                new Object[]{"x!y!x", new String[][]{{"x","2+2"},{"y","3+1"}}},
        };
    }

    @Test
    @Parameters(method = "failedInvalidExpressionTest")
    public void testFailedInvalidExpression(String strExpression){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression).build();
    }
    private Object[] failedInvalidExpressionTest() {
        return new Object[]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{" "}

        };
    }




    @Test
    @Parameters(method = "failedOperatorAdditionTest")
    public void testFailedOpretatorTest(String strExpression, AbstractOperator[] operators){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                                        .withUserDefineOperator(operators).build();
    }


    private Object[] failedOperatorAdditionTest() {
        return new Object[]{
                new Object[]{"2+3", null},
                new Object[]{"2+3", new AbstractOperator[]{}},
                new Object[]{"2+3", new AbstractOperator[]{null}},
                new Object[]{"2+3", new AbstractOperator[]{new AbstractOperator("+",2,true,50000) {
                    @Override
                    protected Double apply(Double... args) {
                        return args[0];
                    }
                }}},

        };
    }


    @Test
    @Parameters(method = "failedFunctionAdditionTest")
    public void testFailedFunctionTest(String strExpression, AbstractFunction[] functions){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withUserDefineFunction(functions).build();
    }


    private Object[] failedFunctionAdditionTest() {
        return new Object[]{
                new Object[]{"2+3", null},
                new Object[]{"2+3", new AbstractFunction[]{}},
                new Object[]{"2+3", new AbstractFunction[]{null}},
                new Object[]{"2+3", new AbstractFunction[]{new AbstractFunction("sin",0) {
                    @Override
                    protected Double apply(Double... args) {
                        return 1d;
                    }
                }}},
                new Object[]{"2+3", new AbstractFunction[]{new AbstractFunction("pi",0) {
                    @Override
                    protected Double apply(Double... args) {
                        return 1d;
                    }
                }}}

        };
    }

    @Test
    @Parameters(method = "failedVariableAdditionTest")
    public void testFailedVariableAdditionTest(String strExpression,
                                               String variableName){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withVariableOrExpressionsNames(variableName).build();

    }


    private Object[] failedVariableAdditionTest() {
        return new Object[]{
            new Object[]{"2+3x",null},
            new Object[]{"2+3x",""},
            new Object[]{"2+3x","pi"},
            new Object[]{"2+3x","sin"},
            new Object[]{"2+3x","+"},
        };

    }

}

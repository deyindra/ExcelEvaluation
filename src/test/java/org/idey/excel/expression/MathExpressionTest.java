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

import java.util.Optional;

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
                .withVariableOrExpressionsNames("~")
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
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
                new Object[]{"2+3", new AbstractOperator[]{new AbstractOperator(">>",2,true,50000) {
                    @Override
                    protected Double apply(Double... args) {
                        return args[0];
                    }
                }}},
                new Object[]{"2+3", new AbstractOperator[]{new AbstractOperator("~",2,true,50000) {
                    @Override
                    protected Double apply(Double... args) {
                        return args[0];
                    }
                }}}
        };
    }


    @Test
    @Parameters(method = "failedFunctionAdditionTest")
    public void testFailedFunctionTest(String strExpression, AbstractFunction[] functions){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withVariableOrExpressionsNames("~")
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
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
                }}},
                new Object[]{"2+3", new AbstractFunction[]{new AbstractFunction("now",0) {
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
                .withVariableOrExpressionsNames(variableName)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                .build();

    }


    private Object[] failedVariableAdditionTest() {
        return new Object[]{
            new Object[]{"2+3x",null},
            new Object[]{"2+3x",""},
            new Object[]{"2+3x","pi"},
            new Object[]{"2+3x","sin"},
            new Object[]{"2+3x","+"},
            new Object[]{"2+3x",">>"},
            new Object[]{"2+3x","now"},
        };

    }

    @Test
    @Parameters(method = "failedVariableValueAdditionTest")
    public void expresessionVariableTest(String strExpression,String expressionVariableName,
                                         String name,Double value){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withVariableOrExpressionsNames(expressionVariableName)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                .build();
        expression.setValue(name,value);
    }

    private Object[] failedVariableValueAdditionTest() {
        return new Object[]{
                new Object[]{"2+3x","x",null,1d},
                new Object[]{"2+3x","x"," ",1d},
                new Object[]{"2+3x","x","x",null},
                new Object[]{"2+3x","x","X",null},
                new Object[]{"2+3x","x","y",1d},
        };
    }


    @Test
    @Parameters(method = "failedVariableExpressionAdditionTest")
    public void expresessionSubExpressionTest(String strExpression,String expressionVariableName,
                                         String name,String subExpresion){
        expectedException.expect(Exception.class);
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withVariableOrExpressionsNames(expressionVariableName)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                .build();
        if(subExpresion != null)
            expression.setExpression(name,new MathExpression.MathExpressionBuilder(subExpresion)
                    .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                    .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                    .build());
        else
            expression.setExpression(name,null);
    }

    private Object[] failedVariableExpressionAdditionTest() {
        return new Object[]{
                new Object[]{"2+3x","x",null,"2+3"},
                new Object[]{"2+3x","x"," ","2+3"},
                new Object[]{"2+3x","x","x",null},
                new Object[]{"2+3x","x","X",null},
                new Object[]{"2+3x","x","y","2+3"},
        };

    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Test
    @Parameters(method = "failednullVariableValueOrInvalidExpressionTest")
    public void nullVariableValueOrInvalidExpressionTest(String strExpression, String variableName,
                                                         Optional<ValueExpPair> expressionOptional,
                                                         boolean expectedResult){
        MathExpression expression = new MathExpression.MathExpressionBuilder(strExpression)
                .withVariableOrExpressionsNames(variableName)
                .withUserDefineOperator(operatorMap.values().toArray(new AbstractOperator[operatorMap.values().size()]))
                .withUserDefineFunction(functionMap.values().toArray(new AbstractFunction[functionMap.values().size()]))
                .build();
        if(expressionOptional.isPresent()){
            ValueExpPair expPair = expressionOptional.get();
            if(expPair.isExpression()){
                expression.setExpression(variableName,expPair.getExpression());
            }else{
                expression.setValue(variableName,expPair.getValue());
            }
        }
        ExpressionValidationResult result = expression.validate();
        if(expectedResult){
            Assert.assertTrue(result.isSuccess());
        }else{
            result.getErrors().forEach(LOGGER::info);
            Assert.assertFalse(result.isSuccess());
        }
    }

    private Object[] failednullVariableValueOrInvalidExpressionTest() {
        return new Object[]{
                new Object[]{"2+3x","x",Optional.empty(),false},
                new Object[]{"2+3x","x",Optional.of(new ValueExpPair(5d)),true},
                new Object[]{"2+3x","x",Optional.of(new ValueExpPair(new MathExpression
                        .MathExpressionBuilder("2+3x").withVariableOrExpressionsNames("x").build())),false},
                new Object[]{"2+3x","x",Optional.of(new ValueExpPair(new MathExpression
                        .MathExpressionBuilder("2+3").build())),true},
                new Object[]{"2+3x","x",Optional.of(new ValueExpPair(new MathExpression
                        .MathExpressionBuilder("2+3x")
                        .withVariableOrExpressionsNames("x").build()
                        .setExpression("x",
                                new MathExpression.MathExpressionBuilder("2+3x")
                                        .withVariableOrExpressionsNames("x").build()))),false},
                new Object[]{"pow(2)","x",Optional.empty(),false},
                new Object[]{"-pipow(2)","x",Optional.empty(),false},
                new Object[]{"2+","x",Optional.empty(),false},
                new Object[]{"+2","x",Optional.empty(),true},
                new Object[]{"+2pi","x",Optional.empty(),true},
                new Object[]{"-pipow(2,3)","x",Optional.empty(),true},
                new Object[]{"-pipow(2,3)","x",Optional.empty(),true},
                new Object[]{"pow(2,3,10)","x",Optional.empty(),false},

        };

    }



}

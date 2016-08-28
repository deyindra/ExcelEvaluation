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
    public void testRPN(String strExpression){
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




}

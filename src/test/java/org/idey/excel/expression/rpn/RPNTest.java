package org.idey.excel.expression.rpn;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.expression.OperatorAndFunctionUtil;
import org.idey.excel.expression.tokenizer.AbstractExpressionToken;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RPNTest extends OperatorAndFunctionUtil{
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Test
    @Parameters(method = "checkExpression")
    public void testRPN(String expression, int expectedSize){
        AbstractExpressionToken[] abstractExpressionTokens =
                RPN.infixToRPN(expression,functionMap,operatorMap,null);
        for(AbstractExpressionToken token:abstractExpressionTokens){
            LOGGER.info(token.toString());
        }
        Assert.assertEquals(abstractExpressionTokens.length,expectedSize);
    }

    private Object[] checkExpression() {
        Object[] objects = new Object[list.size()];
        for(int count=0;count<list.size();count++){
            Expression expression = list.get(count);
            objects[count] = new Object[]{expression.getExpression(),
                    expression.getExpectedOutputOfRPNTest()};
        }
        return objects;
    }


    @Test
    @Parameters(method = "checkFailedExpression")
    public void testFailedRPN(String expression){
        expectedException.expect(Exception.class);
        AbstractExpressionToken[] abstractExpressionTokens = RPN.infixToRPN(expression,null,null,null);
    }

    private Object[] checkFailedExpression() {
        return new Object[]{
            new Object[]{"(2+3*3"},
            new Object[]{"(2+3}*3"},
            new Object[]{"2+3}*3"},
            new Object[]{" "},
            new Object[]{null},
            new Object[]{"pow(2 2,3)"},
            new Object[]{"2+sin+,2"},

        };
    }

}
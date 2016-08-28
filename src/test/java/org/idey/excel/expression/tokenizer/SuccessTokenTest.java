package org.idey.excel.expression.tokenizer;


import org.idey.excel.expression.function.BuiltInFunctions;
import org.idey.excel.expression.operator.BuiltInOperators;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessTokenTest{
    private static final Logger LOGGER = LoggerFactory.getLogger(SuccessTokenTest.class);
    private AbstractExpressionToken token;
    private TokenEnum expectedTokenType;

    public SuccessTokenTest(AbstractExpressionToken token,
                            TokenEnum expectedTokenType) {
        this.expectedTokenType = expectedTokenType;
        this.token = token;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new ArgumentSeparatorExpressionToken(), TokenEnum.TOKEN_SEPARATOR},
                {new CloseParenthesesExpressionToken(), TokenEnum.TOKEN_PARENTHESES_CLOSE},
                {new FunctionExpressionToken(BuiltInFunctions.getBuiltinFunction("sin")), TokenEnum.TOKEN_FUNCTION},
                {new NumberExpressionToken(123d), TokenEnum.TOKEN_NUMBER},
                {new NumberExpressionToken("ABC124".toCharArray(),3,3), TokenEnum.TOKEN_NUMBER},
                {new OpenParenthesesExpressionToken(), TokenEnum.TOKEN_PARENTHESES_OPEN},
                {new OperatorExpressionToken(BuiltInOperators.getBuiltinOperator('^',2)), TokenEnum.TOKEN_OPERATOR},
                {new VariableExpressionToken("XY"), TokenEnum.TOKEN_VARIABLE},
        });
    }


    @Test
    public void test(){
        Assert.assertEquals(expectedTokenType,token.getType());
        LOGGER.info(token.toString());
        switch (expectedTokenType){
            case TOKEN_FUNCTION:
                Assert.assertNotNull(((FunctionExpressionToken)token).getFunction());
                break;
            case TOKEN_OPERATOR:
                Assert.assertNotNull(((OperatorExpressionToken)token).getOperator());
                break;
            case TOKEN_NUMBER:
                Assert.assertNotNull(((NumberExpressionToken)token).getValue());
                break;
            case TOKEN_VARIABLE:
                Assert.assertNotNull(((VariableExpressionToken)token).getName());
                break;
            default:
                break;
        }
    }
}
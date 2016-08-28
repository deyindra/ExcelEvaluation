package org.idey.excel.expression.operator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BuiltInOperatorsTest {
    private char ch;
    private int numberArguments;
    private AbstractOperator expectedOperator;

    public BuiltInOperatorsTest(char ch, int numberArguments, AbstractOperator expectedOperator) {
        this.ch = ch;
        this.numberArguments = numberArguments;
        this.expectedOperator = expectedOperator;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {'+', 1, BuiltInOperators.UNARYPLUS},
                {'-', 1, BuiltInOperators.UNARYMINUS},
                {'+',2, BuiltInOperators.ADDITION},
                {'-',2, BuiltInOperators.SUBTRACTION},
                {'*',2, BuiltInOperators.MUTLIPLICATION},
                {'/',2, BuiltInOperators.DIVISION},
                {'^',2, BuiltInOperators.POWER},
                {'%',2, BuiltInOperators.MODULO},
                {'#',2, null},
                {'/',3, null},
        });
    }

    @Test
    public void test(){
        if(expectedOperator==null){
            Assert.assertNull(BuiltInOperators.getBuiltinOperator(ch,numberArguments));
        }else{
            Assert.assertEquals(expectedOperator, BuiltInOperators.getBuiltinOperator(ch,numberArguments));
        }
    }
}

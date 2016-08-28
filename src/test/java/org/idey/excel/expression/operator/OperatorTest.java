package org.idey.excel.expression.operator;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class OperatorTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "checkValidOperator")
    public void test(String symbol,
                     int numberOfArguments,
                     boolean leftAssociatives,
                     int precedance,
                     final Double... args){
        AbstractOperator dummy = new AbstractOperator(symbol,numberOfArguments,leftAssociatives,precedance) {
            @Override
            protected Double apply(Double... args) {
                return (double)args.length;
            }
        };
        Assert.assertEquals(symbol, dummy.getSymbol());
        Assert.assertEquals(numberOfArguments, dummy.getNumOperands());
        Assert.assertEquals(leftAssociatives, dummy.isLeftAssociative());
        Assert.assertEquals(precedance, dummy.getPrecedence());
        Assert.assertTrue((double)args.length==dummy.checkAndApply(args));
    }

    private Object[] checkValidOperator() {
        return new Object[]{
                new Object[]{"!*",2,true,100001, new Double[]{1d,2d}},
                new Object[]{"^*",1,true,100001, new Double[]{1d}},
        };
    }



    @Test
    @Parameters(method = "checkInvalidOperator")
    public void failureTest(String symbol,
                     int numberOfArguments,
                     boolean leftAssociatives,
                     int precedance){
        expectedException.expect(Exception.class);
        AbstractOperator dummy = new AbstractOperator(symbol,numberOfArguments,leftAssociatives,precedance) {
            @Override
            protected Double apply(Double... args) {
                return (double)args.length;
            }
        };


    }

    private Object[] checkInvalidOperator() {
        return new Object[]{
                new Object[]{"!*",3,true,100001},
                new Object[]{"",2,true,100001},
                new Object[]{null,2,true,100001},
                new Object[]{"23",1,true,100001},
        };
    }

}

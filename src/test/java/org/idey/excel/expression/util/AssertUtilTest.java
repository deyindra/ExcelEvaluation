package org.idey.excel.expression.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RunWith(JUnitParamsRunner.class)
public class AssertUtilTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test(){
        AssertUtil.checkNull("ABC");
        AssertUtil.checkNull(new String[]{"ABC"});
        AssertUtil.checkNull(Arrays.asList("ABC"));
        AssertUtil.checkNull(new String[]{"ABC"},1);
        AssertUtil.checkNull(Arrays.asList("ABC"),1);

    }

    @Test
    @Parameters(method = "checkNull")
    public void testCheckNull(String object){
        expectedException.expect(IllegalArgumentException.class);
        AssertUtil.checkNull(object);
    }

    private Object[] checkNull() {
        return new Object[]{
            new Object[]{null}
        };
    }

    @Test
    @Parameters(method = "checkNullArray")
    public void testCheckNull(String[] object){
        expectedException.expect(IllegalArgumentException.class);
        AssertUtil.checkNull(object);
    }

    private Object[] checkNullArray() {
        return new Object[]{
                new Object[]{null},
                new Object[]{new String[]{}},
                new Object[]{new String[]{"A", null, "B"}}
        };
    }

    @Test
    @Parameters(method = "checkNullCollection")
    public void testCheckNull(Collection<String> object){
        expectedException.expect(IllegalArgumentException.class);
        AssertUtil.checkNull(object);
    }

    private Object[] checkNullCollection() {
        return new Object[]{
                new Object[]{null},
                new Object[]{Collections.emptyList()},
                new Object[]{Arrays.asList("A", null, "B")}
        };
    }

    @Test
    @Parameters(method = "checkArrayWithLength")
    public void testCheckArrayWithLength(String[] array, int length){
        expectedException.expect(IllegalArgumentException.class);
        AssertUtil.checkNull(array,length);
    }

    private Object[] checkArrayWithLength() {
        return new Object[]{
                new Object[]{null,-1},
                new Object[]{null,1},
                new Object[]{new String[]{},1},
                new Object[]{new String[]{"ANC"},2},
                new Object[]{new String[]{null},2}
        };
    }


    @Test
    @Parameters(method = "checkCollectionWithLength")
    public void testCheckArrayWithLength(Collection<String> list, int length){
        expectedException.expect(IllegalArgumentException.class);
        AssertUtil.checkNull(list,length);
    }

    @SuppressWarnings("ConstantConditions")
    private Object[] checkCollectionWithLength() {
        return new Object[]{
                new Object[]{null,-1},
                new Object[]{null,1},
                new Object[]{Collections.emptyList(),1},
                new Object[]{Arrays.asList("ANC"),2},
                new Object[]{Arrays.asList(new String[]{null}),2},
        };
    }
}

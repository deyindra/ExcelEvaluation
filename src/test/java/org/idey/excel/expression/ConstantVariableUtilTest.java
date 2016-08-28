package org.idey.excel.expression;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ConstantVariableUtilTest {
    @Test
    public void testKeySetsOfVariables(){
        Assert.assertEquals(4,ConstantVariableUtil.getAllConstantVariables().size());
    }

    @Test
    public void testAllValues(){
        Map<String,Double> map = ConstantVariableUtil.getConstantVariables();
        Assert.assertEquals((Double) Math.PI,map.get("pi"));
        Assert.assertEquals((Double) Math.PI,map.get("π"));
        Assert.assertEquals((Double)1.61803398874d,map.get("φ"));
        Assert.assertEquals((Double) Math.E,map.get("e"));

    }
}

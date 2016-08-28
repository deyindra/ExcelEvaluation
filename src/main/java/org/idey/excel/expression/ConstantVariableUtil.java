package org.idey.excel.expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConstantVariableUtil {
    private static final Map<String, Double> map = new HashMap<>();
    static{
        map.put("pi", Math.PI);
        map.put("π", Math.PI);
        map.put("φ", 1.61803398874d);
        map.put("e", Math.E);
    }
    public static Map<String, Double> getConstantVariables(){
        return Collections.unmodifiableMap(map);
    }

    public static Set<String> getAllConstantVariables(){
        return Collections.unmodifiableSet(map.keySet());
    }
}

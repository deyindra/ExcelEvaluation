package org.idey.excel.expression.function;

import java.util.HashMap;
import java.util.Map;

/**
 * @author i.dey
 * Class for Built In Functions
 */
public final class BuiltInFunctions {

    //Map which contains all builtin function
    private static final Map<String, AbstractFunction> map = new HashMap<>();

    protected static final AbstractFunction SIN = new AbstractFunction("sin") {
        @Override
        protected Double apply(Double... args) {
            return Math.sin(args[0]);
        }
    };
    protected static final AbstractFunction COS = new AbstractFunction("cos") {
        @Override
        protected Double apply(Double... args) {
            return Math.cos(args[0]);
        }
    };
    protected static final AbstractFunction TAN = new AbstractFunction("tan") {
        @Override
        protected Double apply(Double... args) {
            return Math.tan(args[0]);
        }
    };
    protected static final AbstractFunction LOG = new AbstractFunction("log") {
        @Override
        protected Double apply(Double... args) {
            return Math.log(args[0]);
        }
    };
    protected static final AbstractFunction LOG2 = new AbstractFunction("log2") {
        @Override
        protected Double apply(Double... args) {
            return Math.log(args[0]) / Math.log(2d);
        }
    };
    protected static final AbstractFunction LOG10 = new AbstractFunction("log10") {
        @Override
        protected Double apply(Double... args) {
            return Math.log10(args[0]);
        }
    };
    protected static final AbstractFunction LOG1P = new AbstractFunction("log1p") {
        @Override
        protected Double apply(Double... args) {
            return Math.log1p(args[0]);
        }
    };
    protected static final AbstractFunction ABS = new AbstractFunction("abs") {
        @Override
        protected Double apply(Double... args) {
            return Math.abs(args[0]);
        }
    };
    protected static final AbstractFunction ACOS = new AbstractFunction("acos") {
        @Override
        protected Double apply(Double... args) {
            return Math.acos(args[0]);
        }
    };
    protected static final AbstractFunction ASIN = new AbstractFunction("asin") {
        @Override
        protected Double apply(Double... args) {
            return Math.asin(args[0]);
        }
    };
    protected static final AbstractFunction ATAN = new AbstractFunction("atan") {
        @Override
        protected Double apply(Double... args) {
            return Math.atan(args[0]);
        }
    };
    protected static final AbstractFunction CBRT = new AbstractFunction("cbrt") {
        @Override
        protected Double apply(Double... args) {
            return Math.cbrt(args[0]);
        }
    };
    protected static final AbstractFunction FLOOR = new AbstractFunction("floor") {
        @Override
        protected Double apply(Double... args) {
            return Math.floor(args[0]);
        }
    };
    protected static final AbstractFunction SINH = new AbstractFunction("sinh") {
        @Override
        protected Double apply(Double... args) {
            return Math.sinh(args[0]);
        }
    };
    protected static final AbstractFunction SQRT = new AbstractFunction("sqrt") {
        @Override
        protected Double apply(Double... args) {
            return Math.sqrt(args[0]);
        }
    };
    protected static final AbstractFunction TANH = new AbstractFunction("tanh") {
        @Override
        protected Double apply(Double... args) {
            return Math.tanh(args[0]);
        }
    };
    protected static final AbstractFunction COSH = new AbstractFunction("cosh") {
        @Override
        protected Double apply(Double... args) {
            return Math.cosh(args[0]);
        }
    };
    protected static final AbstractFunction CEIL = new AbstractFunction("ceil") {
        @Override
        protected Double apply(Double... args) {
            return Math.ceil(args[0]);
        }
    };
    protected static final AbstractFunction POWER = new AbstractFunction("pow",2) {
        @Override
        protected Double apply(Double... args) {
            return Math.pow(args[0], args[1]);
        }
    };
    protected static final AbstractFunction EXP = new AbstractFunction("exp") {
        @Override
        protected Double apply(Double... args) {
            return Math.exp(args[0]);
        }
    };
    protected static final AbstractFunction EXPM1 = new AbstractFunction("expm1") {
        @Override
        protected Double apply(Double... args) {
            return Math.expm1(args[0]);
        }
    };
    protected static final AbstractFunction SIGNUM = new AbstractFunction("signum") {
        @Override
        protected Double apply(Double... args) {
            if (args[0] > 0) {
                return 1d;
            } else if (args[0] < 0) {
                return -1d;
            } else {
                return 0d;
            }
        }
    };

    static{
        map.put(SIN.getName(), SIN);
        map.put(COS.getName(), COS);
        map.put(TAN.getName(), TAN);
        map.put(LOG.getName(), LOG);
        map.put(LOG2.getName(), LOG2);
        map.put(LOG10.getName(), LOG10);
        map.put(LOG1P.getName(), LOG1P);
        map.put(ABS.getName(), ABS);
        map.put(ACOS.getName(), ACOS);
        map.put(ASIN.getName(), ASIN);
        map.put(ATAN.getName(), ATAN);
        map.put(CBRT.getName(), CBRT);
        map.put(FLOOR.getName(), FLOOR);
        map.put(SINH.getName(), SINH);
        map.put(SQRT.getName(), SQRT);
        map.put(TANH.getName(), TANH);
        map.put(COSH.getName(), COSH);
        map.put(CEIL.getName(), CEIL);
        map.put(POWER.getName(), POWER);
        map.put(EXP.getName(), EXP);
        map.put(EXPM1.getName(), EXPM1);
        map.put(SIGNUM.getName(), SIGNUM);
    }

    /**
     *
     * @param name function name case Insensitive
     * @return AbstractFunction if available or null
     */
    public static AbstractFunction getBuiltinFunction(final String name) {
        if(name==null || name.trim().equals("")){
            throw new IllegalArgumentException("Invalid Function name ");
        }
        return map.get(name.trim().toLowerCase());
    }

}

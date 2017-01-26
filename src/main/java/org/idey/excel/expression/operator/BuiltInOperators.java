package org.idey.excel.expression.operator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author i.dey
 * Set BuiltIn Operators
 */
public final class BuiltInOperators {


    protected static final AbstractOperator ADDITION =  new AbstractOperator("+", 2, true,
            BuiltInOperatorsPrecedance.PRECEDENCE_ADDITION) {
        @Override
        protected Double apply(final Double... args) {
            return args[0] + args[1];
        }
    };

    protected static final AbstractOperator SUBTRACTION = new AbstractOperator("-", 2, true,
            BuiltInOperatorsPrecedance.PRECEDENCE_SUBTRACTION) {
        @Override
        protected Double apply(final Double... args) {
            return args[0] - args[1];
        }
    };

    protected static final AbstractOperator UNARYMINUS = new AbstractOperator("-", 1, false,
            BuiltInOperatorsPrecedance.PRECEDENCE_UNARY_MINUS) {
        @Override
        protected Double apply(final Double... args) {
            return -args[0];
        }
    };

    protected static final AbstractOperator UNARYPLUS =  new AbstractOperator("+", 1, false,
            BuiltInOperatorsPrecedance.PRECEDENCE_UNARY_PLUS) {
        @Override
        protected Double apply(final Double... args) {
            return args[0];
        }
    };

    protected static final AbstractOperator MUTLIPLICATION = new AbstractOperator("*", 2, true,
            BuiltInOperatorsPrecedance.PRECEDENCE_MULTIPLICATION) {
        @Override
        protected Double apply(final Double... args) {
            return args[0] * args[1];
        }
    };

    protected static final AbstractOperator DIVISION = new AbstractOperator("/", 2, true,
            BuiltInOperatorsPrecedance.PRECEDENCE_DIVISION) {
        @Override
        protected Double apply(final Double... args) {
            if (args[1] == 0d) {
                throw new ArithmeticException("Division by zero!");
            }
            return args[0] / args[1];
        }
    };

    protected static final AbstractOperator POWER = new AbstractOperator("^", 2, false,
            BuiltInOperatorsPrecedance.PRECEDENCE_POWER) {
        @Override
        protected Double apply(final Double... args) {
            return Math.pow(args[0], args[1]);
        }
    };

    protected static final AbstractOperator MODULO = new AbstractOperator("%", 2, true,
            BuiltInOperatorsPrecedance.PRECEDENCE_MODULO) {
        @Override
        protected Double apply(final Double... args) {
            if (args[1] == 0d) {
                throw new ArithmeticException("Division by zero!");
            }
            return args[0] % args[1];
        }
    };

    private static final Set<String> BUILT_IN_SYMBOLS = Collections.unmodifiableSet(
                                                    new HashSet<>(Arrays.asList(ADDITION.getSymbol(),
                                                    SUBTRACTION.getSymbol(),
                                                    MUTLIPLICATION.getSymbol(),
                                                    DIVISION.getSymbol(),
                                                    POWER.getSymbol(),
                                                    MODULO.getSymbol())));
    /**
     * Define operator precedence
     */
    enum BuiltInOperatorsPrecedance{
        PRECEDENCE_ADDITION(500),
        PRECEDENCE_SUBTRACTION(500),
        PRECEDENCE_MULTIPLICATION(1000),
        PRECEDENCE_DIVISION(1000),
        PRECEDENCE_MODULO(1000),
        PRECEDENCE_POWER(10000),
        PRECEDENCE_UNARY_MINUS(5000),
        PRECEDENCE_UNARY_PLUS(5000);
        final int precedance;

        BuiltInOperatorsPrecedance(int precedance) {
            this.precedance = precedance;
        }

        protected int getPrecedance() {
            return precedance;
        }
    }


    /**
     *
     * @param symbol char symbol for operator
     * @param numArguments number of Arguments
     * @return AbstractOperator if match, else null
     */
    public static AbstractOperator getBuiltinOperator(final char symbol,
                                                      final int numArguments) {
        switch(symbol) {
            case '+':
                if (numArguments == 2) {
                    return ADDITION;
                }else if(numArguments == 1){
                    return UNARYPLUS;
                }
            case '-':
                if (numArguments == 2) {
                    return SUBTRACTION;
                }else if(numArguments == 1){
                    return UNARYMINUS;
                }
            case '*':
                if(numArguments == 2)
                    return MUTLIPLICATION;
            case '/':
                if(numArguments == 2)
                    return DIVISION;
            case '^':
                if(numArguments == 2)
                    return POWER;
            case '%':
                if(numArguments == 2)
                    return MODULO;
            default:
                return null;
        }
    }

    /**
     *
     * @param symbol check if a given symbol is part of BuiltInOperator
     * @return true or false
     */
    public static boolean isBuiltInOperatorSymbol(String symbol){
        return BUILT_IN_SYMBOLS.contains(symbol);
    }

}

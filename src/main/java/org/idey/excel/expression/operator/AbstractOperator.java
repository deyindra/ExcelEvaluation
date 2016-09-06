package org.idey.excel.expression.operator;

import org.idey.excel.expression.CoverageIgnore;
import org.idey.excel.expression.util.AssertUtil;

/**
 * @author i.dey
 * Abstract operators which can be used to define operators
 * @see AssertUtil
 */
public abstract class AbstractOperator {
    protected final int numOperands;
    protected final boolean leftAssociative;
    protected final String symbol;
    protected final int precedence;

    /**
     * Create a new operator for use in expressions
     * @param symbol the symbol of the operator
     * @param numberOfOperands the number of operands the operator takes (1 or 2)
     * @param leftAssociative set to true if the operator is left associative, false if it is right associative
     * @param precedence the precedence value of the operator
     */
    public AbstractOperator(String symbol,
                            int numberOfOperands,
                            boolean leftAssociative,
                            int precedence) {
        if(numberOfOperands != 1 && numberOfOperands !=2){
            throw new IllegalArgumentException("Invalid Number of operands");
        }
        this.numOperands = numberOfOperands;
        this.leftAssociative = leftAssociative;
        if(symbol!=null && !symbol.trim().equals("")){
            symbol = symbol.trim();
            for(int i=0;i<symbol.length();i++){
                final char ch = symbol.charAt(i);
                if(!AllowedCharsetUtil.isAllowedOperatorChar(ch)){
                    throw new IllegalArgumentException(String.format("invalid %c is found " +
                            "in symbol %s", ch, symbol));
                }
            }
            this.symbol = symbol.trim().toLowerCase();
        }else{
            throw new IllegalArgumentException("Invalid Operator");
        }
        this.precedence = precedence;
    }

    AbstractOperator(String symbol,
                            int numberOfOperands,
                            boolean leftAssociative,
                            BuiltInOperators.BuiltInOperatorsPrecedance precedance) {
        this(symbol,numberOfOperands,leftAssociative,precedance.getPrecedance());
    }


    /**
     * Get the number of operands
     * @return the number of operands
     */
    public int getNumOperands() {
        return numOperands;
    }
    /**
     * Check if the operator is left associative
     * @return true os the operator is left associative, false otherwise
     */
    public boolean isLeftAssociative() {
        return leftAssociative;
    }

    /**
     * Get the operator symbol
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Check the precedence value for the operator
     * @return the precedence value
     */
    public int getPrecedence() {
        return precedence;
    }


    /**
     *
     * @param args Double Arguments
     * @return Double check first if the numbef of aruments are not null or equal to numberOfOperands
     * @throws IllegalArgumentException in case of assertion failed
     */
    public Double checkAndApply(Double ... args){
        AssertUtil.checkNull(args,numOperands);
        return apply(args);
    }

    /**
     * Apply the operation on the given operands
     * @param args the operands for the operation
     * @return the calculated result of the operation
     */
    protected abstract Double apply(Double ... args);

    @Override
    public String toString() {
        return "AbstractOperator{" +
                "numOperands=" + numOperands +
                ", leftAssociative=" + leftAssociative +
                ", symbol='" + symbol + '\'' +
                ", precedence=" + precedence +
                '}';
    }

    @CoverageIgnore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractOperator that = (AbstractOperator) o;

        return numOperands ==
                that.numOperands && leftAssociative ==
                that.leftAssociative && precedence == that.precedence &&
                (symbol != null ? symbol.equals(that.symbol) : that.symbol == null);

    }

    @CoverageIgnore
    @Override
    public int hashCode() {
        int result = numOperands;
        result = 31 * result + (leftAssociative ? 1 : 0);
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + precedence;
        return result;
    }
}

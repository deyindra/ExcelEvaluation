package org.idey.excel.expression.function;

import org.idey.excel.expression.util.AssertUtil;

/**
 * A class representing a Function which can be used in an expression
 */
public abstract class AbstractFunction {
    protected final String name;

    protected final int numArguments;

    /**
     * Create a new Function with a given name and number of arguments
     *
     * @param name the name of the Function
     * @param numArguments the number of arguments the function takes
     */
    protected AbstractFunction(String name, int numArguments) {
        if (numArguments < 0) {
            throw new IllegalArgumentException("The number of function arguments can not be less than 0 for '" +
                    name + "'");
        }
        if (!AllowedFunctionUtil.isValidFunctionName(name)) {
            throw new IllegalArgumentException("The function name '" + name + "' is invalid");
        }
        this.name = name.trim().toLowerCase();
        this.numArguments = numArguments;
    }

    /**
     * Create a new Function with a given name that takes a single argument
     *
     * @param name the name of the Function
     */
    protected AbstractFunction(String name) {
        this(name, 1);
    }

    /**
     * Get the name of the Function
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the number of arguments for this function
     *
     * @return the number of arguments
     */
    public int getNumArguments() {
        return numArguments;
    }


    /**
     *
     * @param args Double Arguments
     * @return Double check first if the numbef of aruments are not null or equal to numberOfOperands
     * @throws IllegalArgumentException in case of assertion failed
     */
    public Double checkAndApply(Double ... args){
        AssertUtil.checkNull(args,numArguments);
        return apply(args);
    }

    /**
     * Apply the operation on the given operands
     * @param args the operands for the operation
     * @return the calculated result of the operation
     */
    protected abstract Double apply(Double ... args);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractFunction that = (AbstractFunction) o;

        return numArguments == that.numArguments &&
                (name != null ? name.equals(that.name) : that.name == null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + numArguments;
        return result;
    }

    @Override
    public String toString() {
        return "AbstractFunction{" +
                "name='" + name + '\'' +
                ", numArguments=" + numArguments +
                '}';
    }

}

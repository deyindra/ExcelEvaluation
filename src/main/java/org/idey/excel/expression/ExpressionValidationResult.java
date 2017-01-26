package org.idey.excel.expression;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author i.dey
 * Class represents {@link MathExpression} results return true if Success else return errors
 */
public final class ExpressionValidationResult {
    private Set<String> errors = new HashSet<>();
    private boolean isSuccess;
    /**
     *  SUCCESS which return {@link ExpressionValidationResult#isSuccess} true
     */
    public static final ExpressionValidationResult SUCCESS =
            new ExpressionValidationResult(true);

    /**
     * Default constructor with isSuccess set to false
     */
    public ExpressionValidationResult() {
        this(false);
    }

    private ExpressionValidationResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     *
     * @param error to errors {@link HashSet}
     * @return {@link ExpressionValidationResult}
     * @throws UnsupportedOperationException in case isSuccess set to true
     * @throws IllegalArgumentException in case error is null or empty
     */
    public ExpressionValidationResult addError(String error){
        if(isSuccess){
            throw new UnsupportedOperationException("Operation can be permissted with Success status");
        }else{
            if(error==null || error.trim().equals("")){
                throw new IllegalArgumentException("Invalid Error String");
            }
            errors.add(error.trim());
            return this;
        }
    }

    /**
     *
     * @param errors add multiple errors to errors {@link HashSet}
     * @return {@link ExpressionValidationResult}
     * @throws UnsupportedOperationException in case isSuccess set to true
     * @throws IllegalArgumentException in case errors is null or empty or individual error is null or empty
     */
    public ExpressionValidationResult addError(String... errors){
        if(errors==null || errors.length==0){
            throw new IllegalArgumentException("Invalid Argument");
        }
        for(String error:errors){
            addError(error);
        }
        return this;
    }

    /**
     *
     * @return {@link Collections#unmodifiableSet(Set)} for error
     */
    public Set<String> getErrors() {
        return Collections.unmodifiableSet(errors);
    }

    /**
     *
     * @return isSuccess - true or false
     */
    public boolean isSuccess() {
        return isSuccess;
    }
}

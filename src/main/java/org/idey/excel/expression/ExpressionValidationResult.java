package org.idey.excel.expression;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ExpressionValidationResult {
    private Set<String> errors = new HashSet<>();
    private boolean isSuccess;

    public static final ExpressionValidationResult SUCCESS =
            new ExpressionValidationResult(true);

    public ExpressionValidationResult() {
        this(false);
    }

    private ExpressionValidationResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

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

    public ExpressionValidationResult addError(String... errors){
        if(errors==null || errors.length==0){
            throw new IllegalArgumentException("Invalid Argument");
        }
        for(String error:errors){
            addError(error);
        }
        return this;
    }


    public Set<String> getErrors() {
        return Collections.unmodifiableSet(errors);
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}

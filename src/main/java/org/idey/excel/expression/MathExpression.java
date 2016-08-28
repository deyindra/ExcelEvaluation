package org.idey.excel.expression;

import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.expression.function.BuiltInFunctions;
import org.idey.excel.expression.operator.AbstractOperator;
import org.idey.excel.expression.operator.BuiltInOperators;
import org.idey.excel.expression.rpn.RPN;
import org.idey.excel.expression.tokenizer.*;

import java.util.*;

public class MathExpression {
    private AbstractExpressionToken[] tokens;
    private Map<String, Double> variables;

    private MathExpression(AbstractExpressionToken[] tokens,
                           Map<String, Double> variables) {
        this.tokens = tokens;
        this.variables = variables;
    }

    public ExpressionValidationResult validate(){
        final List<String> errors = new ArrayList<>(0);
        for (final AbstractExpressionToken t : this.tokens) {
            if (t.getType() == TokenEnum.TOKEN_VARIABLE) {
                final String var = ((VariableExpressionToken) t).getName();
                if (!variables.containsKey(var)) {
                    errors.add(String.format("The setVariable '%s' has not been set",var));
                }
            }
        }
        int count = 0;
        for (AbstractExpressionToken tok : this.tokens) {
            switch (tok.getType()) {
                case TOKEN_NUMBER:
                case TOKEN_VARIABLE:
                    count++;
                    break;
                case TOKEN_FUNCTION:
                    final AbstractFunction func = ((FunctionExpressionToken) tok).getFunction();
                    final int argsNum = func.getNumArguments();
                    if (argsNum > count) {
                        errors.add("Not enough arguments for '" + func.getName() + "'");
                    }
                    if (argsNum > 1) {
                        count -= argsNum - 1;
                    } else if (argsNum == 0) {
                        count++;
                    }
                    break;
                case TOKEN_OPERATOR:
                    AbstractOperator op = ((OperatorExpressionToken) tok).getOperator();
                    if (op.getNumOperands() == 2) {
                        count--;
                    }
                    break;
            }
            if (count < 1) {
                errors.add("Too many operators");
                return new ExpressionValidationResult().addError(errors.toArray(new String[errors.size()]));
            }
        }
        if (count > 1) {
            errors.add("Too many operands");
        }
        return errors.size() == 0 ? ExpressionValidationResult.SUCCESS :
                new ExpressionValidationResult()
                .addError(errors.toArray(new String[errors.size()]));
    }

    public double evaluate() {
        final LinkedList<Double> output = new LinkedList<>();
        for (AbstractExpressionToken t : tokens) {
            if (t.getType() == TokenEnum.TOKEN_NUMBER) {
                output.push(((NumberExpressionToken) t).getValue());
            } else if (t.getType() == TokenEnum.TOKEN_VARIABLE) {
                final String name = ((VariableExpressionToken) t).getName();
                final Double value = this.variables.get(name);
                if (value == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                }
                output.push(value);
            } else if (t.getType() == TokenEnum.TOKEN_OPERATOR) {
                OperatorExpressionToken op = (OperatorExpressionToken) t;
                if (output.size() < op.getOperator().getNumOperands()) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + op.getOperator().getSymbol() + "' operator");
                }
                if (op.getOperator().getNumOperands() == 2) {
                    /* pop the operands and push the result of the operation */
                    double rightArg = output.pop();
                    double leftArg = output.pop();
                    output.push(op.getOperator().checkAndApply(leftArg, rightArg));
                } else if (op.getOperator().getNumOperands() == 1) {
                    /* pop the operand and push the result of the operation */
                    double arg = output.pop();
                    output.push(op.getOperator().checkAndApply(arg));
                }
            } else if (t.getType() == TokenEnum.TOKEN_FUNCTION) {
                FunctionExpressionToken func = (FunctionExpressionToken) t;
                final int numArguments = func.getFunction().getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for '" + func.getFunction().getName() + "' function");
                }
                /* collect the arguments from the stack */
                Double[] args = new Double[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = output.pop();
                }
                output.push(func.getFunction().checkAndApply(args));
            }
        }
        if (output.size() > 1) {
            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        return output.pop();
    }

    public static class MathExpressionBuilder{
        private final String expression;
        private final Map<String, Double> userDefinedVariables;
        private final Map<String, AbstractOperator> userDefinedOperators;
        private final Map<String, AbstractFunction> userDefinedFunctions;

        public MathExpressionBuilder(final String expression) {
            if (expression==null || expression.trim().length() == 0) {
                throw new IllegalArgumentException("The expression can not be empty");
            }
            this.expression = expression.trim();
            userDefinedFunctions = new HashMap<>();
            userDefinedVariables = new HashMap<>();
            userDefinedOperators = new HashMap<>();
            userDefinedVariables.putAll(ConstantVariableUtil.getConstantVariables());
        }

        private String checkName(String name){
            if(name==null || name.trim().length()==0){
                throw new IllegalArgumentException("Invalid Variable/Function/Operator name");
            }
            name = name.trim().toLowerCase();
            if(userDefinedVariables.containsKey(name) ||
                    userDefinedFunctions.containsKey(name) ||
                    userDefinedOperators.containsKey(name) ||
                    BuiltInFunctions.getBuiltinFunction(name) != null ||
                    BuiltInOperators.isBuiltInOperatorSymbol(name)
              ){
                throw new IllegalArgumentException(String.format("%s is already registered..", name));
            }
            return name;
        }

        public MathExpressionBuilder withVariables(String name, double value){
            name = checkName(name);
            userDefinedVariables.put(name,value);
            return this;
        }

        public MathExpressionBuilder withExpression(String name, MathExpression expression){
            if(expression==null){
                throw new IllegalArgumentException("Invalid Expression");
            }else{
                ExpressionValidationResult result = expression.validate();
                if(!result.isSuccess()){
                    StringBuilder builder = new StringBuilder("");
                    String seperator = "";
                    Set<String> errors = result.getErrors();
                    for(String value:errors){
                        builder.append(seperator).append(value);
                        seperator="\n";
                    }
                    throw new IllegalArgumentException(builder.toString());
                }else{
                    return withVariables(name,expression.evaluate());
                }
            }
        }

        public MathExpressionBuilder withUserDefineFunction(AbstractFunction function){
            if(function==null){
                throw new IllegalArgumentException("Invalid Function");
            }
            String name = function.getName();
            name = checkName(name);
            userDefinedFunctions.put(name,function);
            return this;
        }

        public MathExpressionBuilder withUserDefineFunction(AbstractFunction... functions){
            if(functions == null || functions.length==0){
                throw new IllegalArgumentException("Invalid Functions");
            }
            for(AbstractFunction function:functions){
                withUserDefineFunction(function);
            }
            return this;
        }

        public MathExpressionBuilder withUserDefineOperator(AbstractOperator operator){
            if(operator==null){
                throw new IllegalArgumentException("Invalid Operator");
            }
            String name = operator.getSymbol();
            name = checkName(name);
            userDefinedOperators.put(name,operator);
            return this;
        }

        public MathExpressionBuilder withUserDefineOperator(AbstractOperator... operators){
            if(operators == null || operators.length==0){
                throw new IllegalArgumentException("Invalid Operators");
            }
            for(AbstractOperator operator:operators){
                withUserDefineOperator(operator);
            }
            return this;
        }

        public MathExpression build(){
            AbstractExpressionToken[] tokens = RPN.infixToRPN(expression,userDefinedFunctions,userDefinedOperators,userDefinedVariables.keySet());
            return new MathExpression(tokens,userDefinedVariables);
        }
    }

}

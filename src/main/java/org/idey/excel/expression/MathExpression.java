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
    private Map<String, ValueExpPair> variables;

    private MathExpression(AbstractExpressionToken[] tokens,
                           Map<String, ValueExpPair> variables) {
        this.tokens = tokens;
        this.variables = variables;
    }

    private MathExpression setVariableOrExpression(String name, Object value, boolean isExpression){
        if(name!=null && name.trim().length()!=0){
            name = name.trim().toLowerCase();
        }else{
            throw new IllegalArgumentException("Name can not be null");
        }
        if(value == null){
            throw new IllegalArgumentException((!isExpression? "Invalid Value" : "Invalid Expression"));
        }
        if(!variables.containsKey(name)){
            throw new IllegalArgumentException(String.format("%s is not registered variable",name));
        }
        if(!isExpression) {
            variables.put(name, new ValueExpPair((Double) value));
        }else {
            variables.put(name, new ValueExpPair((MathExpression) value));
        }
        return this;
    }

    public MathExpression setValue(String name, Double value){
        return setVariableOrExpression(name,value,false);
    }

    public MathExpression setExpression(String name, MathExpression value){
        return setVariableOrExpression(name,value,true);
    }

    public ExpressionValidationResult validate(){
        final List<String> errors = new ArrayList<>(0);
        for (final AbstractExpressionToken t : this.tokens) {
            if (t.getType() == TokenEnum.TOKEN_VARIABLE) {
                final String var = ((VariableExpressionToken) t).getName();
                ValueExpPair expPair = variables.get(var);
                if (expPair == null) {
                    errors.add(String.format("The setVariable '%s' has not been set",var));
                }else{
                    if(expPair.isExpression()){
                        MathExpression subExpression = expPair.getExpression();
                        ExpressionValidationResult result = subExpression.validate();
                        if(!result.isSuccess()){
                            errors.addAll(result.getErrors());
                        }
                    }
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
                        errors.add(String.format("Not enough arguments for '%s'",func.getName()));
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
                ValueExpPair valueExpPair = this.variables.get(name);
                if (valueExpPair == null) {
                    throw new IllegalArgumentException(String.format("No value has been set for the setVariable '%s'",name));
                }else{
                  Double value;
                  if(!valueExpPair.isExpression()){
                      value = valueExpPair.getValue();
                  }else{
                      MathExpression subExpression = valueExpPair.getExpression();
                      value = subExpression.evaluate();
                  }
                  output.push(value);
                }
            } else if (t.getType() == TokenEnum.TOKEN_OPERATOR) {
                OperatorExpressionToken op = (OperatorExpressionToken) t;
                if (output.size() < op.getOperator().getNumOperands()) {
                    throw new IllegalArgumentException(String.format("Invalid number of operands available for '%s' operator",op.getOperator().getSymbol()));
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
                    throw new IllegalArgumentException(String.format("Invalid number of arguments available for '%s' function",func.getFunction().getName()));
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
        private final Map<String, ValueExpPair> userDefinedVariables;
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

        public MathExpressionBuilder withVariableOrExpressionsNames(String name){
            name = checkName(name);
            userDefinedVariables.put(name,null);
            return this;
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
            AbstractExpressionToken[] tokens = RPN.infixToRPN(expression,
                    userDefinedFunctions,userDefinedOperators,userDefinedVariables.keySet());
            return new MathExpression(tokens,userDefinedVariables);
        }
    }


    private static class ConstantVariableUtil {
        private static final Map<String, ValueExpPair> map = new HashMap<>();
        static{
            map.put("pi", new ValueExpPair(Math.PI));
            map.put("π", new ValueExpPair(Math.PI));
            map.put("φ", new ValueExpPair(1.61803398874d));
            map.put("e", new ValueExpPair(Math.E));
        }
        private static Map<String, ValueExpPair> getConstantVariables(){
            return Collections.unmodifiableMap(map);
        }

        private static Set<String> getAllConstantVariables(){
            return Collections.unmodifiableSet(map.keySet());
        }
    }

}

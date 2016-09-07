package org.idey.excel.expression;

import au.com.bytecode.opencsv.CSVReader;
import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.expression.operator.AbstractOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class OperatorAndFunctionUtil {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OperatorAndFunctionUtil.class);

    protected Map<String, AbstractFunction> functionMap;
    protected Map<String, AbstractOperator> operatorMap;
    protected List<Expression> list;

    private static final String EXPRESSION_FILE_PATH = "/expressionList.cvs";

    protected OperatorAndFunctionUtil(){
        functionMap = new HashMap<>();
        operatorMap = new HashMap<>();
        list = new ArrayList<>();
        for(UserDefineOperatorEnum userDefineOperatorEnum
                :UserDefineOperatorEnum.values()){
            AbstractOperator operator = userDefineOperatorEnum.operator;
            operatorMap.put(operator.getSymbol(),operator);
        }
        for(UserDefineFunctionEnum userDefineFunctionEnum
                :UserDefineFunctionEnum.values()){
            AbstractFunction function = userDefineFunctionEnum.function;
            functionMap.put(function.getName(),function);
        }
        try(CSVReader reader = new CSVReader(
                new FileReader(OperatorAndFunctionUtil.class.getResource(EXPRESSION_FILE_PATH).getPath()),',','"')){
            List<String[]> lists = reader.readAll();
            list.addAll(lists.stream().map(array -> new Expression(array[0], Integer.parseInt(array[1]),
                    Integer.parseInt(array[2]))).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }



    enum UserDefineOperatorEnum{
        FACTORIAL(
                new AbstractOperator("!", 1, true, 10001) {
                    @Override
                    protected Double apply(Double... args) {
                        final int arg = args[0].intValue();
                        if ((double) arg != args[0]) {
                            throw new IllegalArgumentException("Operand for factorial has to be an integer");
                        }
                        if (arg < 0) {
                            throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                        }
                        double result = 1;
                        for (int i = 1; i <= arg; i++) {
                            result *= i;
                        }
                        return result;
                    }
                }
        ),
        DOUBLEFACTORIAL(
               new AbstractOperator(">>", 2, true, 10002) {
                   @Override
                   protected Double apply(Double... args) {
                       int args0 = getInteger(args[0]);
                       int args1 = getInteger(args[1]);
                       return (double)(args0>>args1);
                   }
                   private int getInteger(Double value){
                       final int intValue = value.intValue();
                       if ((double) intValue != value) {
                           throw new IllegalArgumentException("Operand for bit shift has to be an integer");
                       }
                       return intValue;
                   }
               }
        );
        private AbstractOperator operator;
        UserDefineOperatorEnum(AbstractOperator operator) {
            this.operator = operator;
        }
    }


  enum UserDefineFunctionEnum{
        NOW(
            new AbstractFunction("now",0) {
                @Override
                public Double apply(Double... args) {
                    return 1d;
                }
        }
      );
    private AbstractFunction function;
    UserDefineFunctionEnum(AbstractFunction function) {
        this.function = function;
    }
  }

  protected static class Expression{
      private String expression;
      private int expectedOutputOfRPNTest;
      private int expectedOutputOfExpressionTest;

      public Expression(String expression,
                        int expectedOutputOfRPNTest,
                        int expectedOutputOfExpressionTest) {
          this.expression = expression;
          this.expectedOutputOfRPNTest = expectedOutputOfRPNTest;
          this.expectedOutputOfExpressionTest = expectedOutputOfExpressionTest;
      }

      public String getExpression() {
          return expression;
      }

      public int getExpectedOutputOfRPNTest() {
          return expectedOutputOfRPNTest;
      }

      public int getExpectedOutputOfExpressionTest() {
          return expectedOutputOfExpressionTest;
      }

      @Override
      public String toString() {
          return "Expression{" +
                  "expression='" + expression + '\'' +
                  ", expectedOutputOfExpressionTest=" + expectedOutputOfExpressionTest +
                  ", expectedOutputOfRPNTest=" + expectedOutputOfRPNTest +
                  '}';
      }
  }

}

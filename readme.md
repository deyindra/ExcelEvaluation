# ExcelEvaluation
[![Build Status](https://travis-ci.org/deyindra/ExcelEvaluation.svg?branch=master)](https://travis-ci.org/deyindra/ExcelEvaluation)
[![Code Coverage](https://codecov.io/gh/deyindra/ExcelEvaluation/branch/master/graph/badge.svg)](https://codecov.io/gh/deyindra/ExcelEvaluation/branch/master)

OverView
===========================
ExcelEvaluation is a API to resolve set of math expression which are given in a nXm metrics where n is number of rows and m is number of cols.
It has two compoments.
##### 1. MathExpression
##### 2. Excel

Like an Excel Sheet, each cell will be named with an encoded value. E.g, for cell at row 1 and col 1 will be called as "A1"
Similarly 
##### * Cell at row 1 and col 27 will be called as AA1
##### * Cell at row 1 and col 2 will be called as B1
##### * Each cell value will be initialized with 0.0d.
 
Class Diagram
============================
![class diagram](ExcelEvaluation.jpg)

API and Usage (MathExpression)
====================
##### 1. Evaluating Simple Math Expression
```java
    import org.idey.excel.expression.MathExpression;
    public class Sample{
        public static void main(String[] args){
          MathExpression expression = new MathExpression.MathExpressionBuilder("0+1").build();
          System.out.println(expression.evaluate()); //print 1.0
        }
    }
```
##### 2. Evaluating Math Expression with Built In Functions
```java
   import org.idey.excel.expression.MathExpression;
       public class Sample{
           public static void main(String[] args){
             MathExpression expression = new MathExpression.MathExpressionBuilder("pow(2,3)").build();
             System.out.println(expression.evaluate()); //print 8.0
           }
       } 
```
```
Following Built In Functions are available as part of this package - "sin", "cos", "tan", "log", "log2", "log10", "log1p", 
"abs", "acos", "asin", "atan", "cbrt", "floor", "sinh", "sqrt", "tanh", "cosh", "ceil", "pow", "exp", "expm1", "signum"
```
##### 3. Evaluating Math Expression with Built In Operators
```java
     import org.idey.excel.expression.MathExpression;
       public class Sample{
           public static void main(String[] args){
             MathExpression expression = new MathExpression.MathExpressionBuilder("2^3").build();
             System.out.println(expression.evaluate()); //print 8.0
           }
       } 
```
```
Following Built In Operators are available as part of this package - "+", "-", "*", "/", "+ (unnary)", "- (unnary)", 
"^", "%"
```
##### 4. Evaluating Math Expression with Built In Variables
```java
   import org.idey.excel.expression.MathExpression;
      /**
        * print {@link Math#PI} value  
      **/
      public class Sample{
          public static void main(String[] args){
            MathExpression expression = new MathExpression.MathExpressionBuilder("pi").build();
            System.out.println(expression.evaluate()); 
          }
      } 
```
```
Following Built In Variables are available as part of this package - "pi", "π", "φ", "e"
```
##### 5. Evaluating Math Expression with Custom Function
```java
    import org.idey.excel.expression.MathExpression;
    import org.idey.excel.expression.function.AbstractFunction;
    
    public class Sample{
        public static void main(String[] args){
          AbstractFunction fn = new AbstractFunction("now",0) {
              @Override
              protected Double apply(Double... args) {
                  return 1d;
              }

          };
          MathExpression expression = new MathExpression.MathExpressionBuilder("now").withUserDefineFunction(fn).build();
          System.out.println(expression.evaluate()); 
        }
    }    
```
##### 6. Evaluating Math Expression with Custom Operator
```java
    import org.idey.excel.expression.MathExpression;
    import org.idey.excel.expression.operator.AbstractOperator;
    
    public class Sample{
        public static void main(String[] args){
          AbstractOperator operator = new AbstractOperator(">>", 2, true, 10002) {
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
         };
          MathExpression expression = new MathExpression.MathExpressionBuilder("1>>2").withUserDefineOperator(operator).build();
          System.out.println(expression.evaluate()); 
        }
    }
```
##### 7. Evaluating Math Expression with Substitue variable
```java
    import org.idey.excel.expression.MathExpression;
    
    public class Sample{
        public static void main(String[] args){
          MathExpression expression = new MathExpression.MathExpressionBuilder("x+2").withVariableOrExpressionsNames("x").build();
          expression.setValue("x",2d);
          System.out.println(expression.evaluate()); //print 4.0
        }
    }
```    
##### 8. Evaluating Math Expression with Substitue expression
```java
    import org.idey.excel.expression.MathExpression;
    
    public class Sample{
        public static void main(String[] args){
          MathExpression expression = new MathExpression.MathExpressionBuilder("x+2").withVariableOrExpressionsNames("x").build();
          MathExpression subExpression = new MathExpression.MathExpressionBuilder("(2*3)").build();
          expression.setExpression("x",subExpression);
          System.out.println(expression.evaluate()); //print 8.0
        }
    }
```
    
API and Usage (Excel)
====================    
##### 1. Setting up Excel Object with N rows and M cols
```java
    
   import org.idey.excel.Excel;
   public class Sample{
    public static void main(String[] args){
      Excel excel = new Excel.ExcelBuilder(5,6).build();
    }
   } 
```
##### 2. Setting up Excel Object with N rows and M cols and set of UDF (user defined functions)
```java
   import org.idey.excel.Excel;
   import org.idey.excel.expression.function.AbstractFunction;
   
   public class Sample{
       public static void main(String[] args){
         AbstractFunction[] udfs = new AbstractFunction[]{
                                           new AbstractFunction("now", 0){
                                               @Override
                                               protected Double apply(Double... args) {
                                                   return 1d;
                                               }
                                           }
                                   };
         /*
          * this will build excel object with set of built In functions 
          * (refer {@link org.idey.excel.expression.function.BuiltInFunctions}) and 
          * set of UDFS (refer {@link AbstractFunction})  
          */
         Excel excel = new Excel.ExcelBuilder(5,6, udfs).build(); 
       }
   }

```

##### 3. Adding Expression to Excel Object
```java
   import org.idey.excel.Excel;

   public class Sample{
       public static void main(String[] args){
          Excel excel = new Excel.ExcelBuilder(5,6).build();
          /*
          * Here Cell B1 will be evaluated as 5.0d and Cell A1 hence it depends on value B1
          * will be evaluated as 6*5.0d which will be 30.0d.
          */
          excel.addExpression("2+3", "B1").addExpression("6*B1", "A1", "B1");   
       }
   } 
```

##### 4. Finally evaluating all the cell values of excel object
```java
   import org.idey.excel.Excel;
   import org.idey.excel.ExcelData; 

   public class Sample{
       public static void main(String[] args){
          Excel excel = new Excel.ExcelBuilder(5,6).build();
          /*
          * Here Cell B1 will be evaluated as 5.0d and Cell A1 hence it depends on value B1
          * will be evaluated as 6*5.0d which will be 30.0d.
          */
          excel.addExpression("2+3", "B1").addExpression("6*B1", "A1", "B1");
          /* In case any dependent cell evaluation return error then 
           * actual cell also be evaluated as Error  
           */
          ExcelData[][] data = excel.evaluateData();
       }
   } 
```
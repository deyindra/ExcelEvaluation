# ExcelEvaluation
[![Build Status](https://travis-ci.org/deyindra/ExcelEvaluation.svg?branch=master)](https://travis-ci.org/deyindra/ExcelEvaluation)
[![Code Coverage](https://codecov.io/gh/deyindra/ExcelEvaluation/branch/master/graph/badge.svg)](https://codecov.io/gh/deyindra/ExcelEvaluation/branch/master)

OverView
===========================
ExcelEvaluation is a API to resolve set of math expression which are given in a nXm metrics where n is number of rows and m is number of cols.
It has two compoments.
1. MathExpression
2. Excel
 
Class Diagram
============================
![class diagram](ExcelEvaluation.jpg)

API and Usage
====================
1. Evaluating Simple Math Expression
```java
    import org.idey.excel.expression.MathExpression;
    public class Sample{
        public static void main(String[] args){
          MathExpression expression = new MathExpression.MathExpressionBuilder("0+1").build();
          System.out.printf(expression.evaluate()); //print 1.0
        }
    }
```
2. Evaluating Math Expression with Built In Functions
```java
   import org.idey.excel.expression.MathExpression;
       public class Sample{
           public static void main(String[] args){
             MathExpression expression = new MathExpression.MathExpressionBuilder("pow(2,3)").build();
             System.out.printf(expression.evaluate()); //print 8.0
           }
       } 
```
```
Following Built In Functions are available as part of this package - "sin", "cos", "tan", "log", "log2", "log10", "log1p", 
"abs", "acos", "asin", "atan", "cbrt", "floor", "sinh", "sqrt", "tanh", "cosh", "ceil", "pow", "exp", "expm1", "signum"
```
3. Evaluating Math Expression with Built In Operators
```java
     import org.idey.excel.expression.MathExpression;
       public class Sample{
           public static void main(String[] args){
             MathExpression expression = new MathExpression.MathExpressionBuilder("2^3").build();
             System.out.printf(expression.evaluate()); //print 8.0
           }
       } 
```
```
Following Built In Operators are available as part of this package - "+", "-", "*", "/", "+ (unnary)", "- (unnary)", 
"^", "%"
```
3. Evaluating Math Expression with Built In Variables
```java
   import org.idey.excel.expression.MathExpression;
      /**
        * print {@link Math#PI} value  
      **/
      public class Sample{
          public static void main(String[] args){
            MathExpression expression = new MathExpression.MathExpressionBuilder("pi").build();
            System.out.printf(expression.evaluate()); 
          }
      } 
```
```
Following Built In Variables are available as part of this package - "pi", "π", "φ", "e"
```

    
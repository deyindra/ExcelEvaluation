package org.idey.excel;

import org.idey.excel.expression.MathExpression;
import org.idey.excel.expression.MathExpression.MathExpressionBuilder;
import org.idey.excel.expression.function.AbstractFunction;
import org.idey.excel.graph.Graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author i.dey
 * Excel object
 */
public final class Excel {
    protected static final String CELL_NAME = "%s%d";
    private static final MathExpression DEFAULT_MATHEXPRESSION = new MathExpression.MathExpressionBuilder("0").build();
    private Graph<String> graph;
    private Map<String, MathExpression> mapExMap;
    private final int rows;
    private final int cols;
    private final ExcelBuilder builder;

    private Excel(Graph<String> graph,
                  Map<String, MathExpression> mapExMap, int rows,
                  int cols, ExcelBuilder builder) {
        this.graph = graph;
        this.mapExMap = mapExMap;
        this.rows = rows;
        this.cols = cols;
        this.builder = builder;
    }

    /**
     *
     * @return array of {@link ExcelData}
     */
    public ExcelData[][] evaluateData(){
        Map<String, ExcelData> computedValue = new HashMap<>();
        Set<String> vertices = graph.getAllVertices();
        for(String vertex:vertices){
            if(!computedValue.containsKey(vertex)){
               evaluateData(computedValue,vertex);
            }
        }
        ExcelData[][] array = new ExcelData[rows][cols];
        for(int row=1;row<=this.rows;row++){
            for(int col=1;col<=this.cols;col++){
                String vertex = String.format(CELL_NAME,
                        PositiveBaseConverterEnum.EXCEL_ENCODING.encode(col),row).toLowerCase();
                array[row-1][col-1]=computedValue.get(vertex);
            }
        }
        return array;
    }

    /**
     * clear the content of Excel Object
     */
    public void clear(){
        this.builder.initialize();
    }


    private void evaluateData(Map<String, ExcelData> computedValue,String vertex){
        Set<String> children = graph.getAllImediateNeighbours(vertex);
        for(String child:children){
            if(!computedValue.containsKey(child)){
                evaluateData(computedValue, child);
            }
        }
        boolean isError = false;
        MathExpression ex = mapExMap.get(vertex);
        for(String child:children){
            ExcelData data = computedValue.get(child);
            if(data.isError()){
                isError = true;
                break;
            }else{
                ex.setValue(child,data.getData());
            }
        }
        ExcelData data;
        if(isError){
            data = new ExcelData("Error in computing due to error in dependency cell");
        }else{
            try{
                data = new ExcelData(ex.evaluate());
            }catch (Exception e){
                data = new ExcelData(Arrays.deepToString(e.getStackTrace()));
            }
        }
        computedValue.put(vertex,data);
    }

    /**
     *
     * @param expression String expression
     * @param cellName name of the cell it will be A1, A2, B1, B2 so on.....
     * @param dependencyCellNames dependency cell names to evaluate the expression
     * @return Excel object
     * @throws IllegalArgumentException in case expression is null or empty or cellName or dependency cellNames
     * are not valid
     */
    public Excel addExpression(String expression, final String cellName,
                               final String... dependencyCellNames){
        if(expression==null || "".equals(expression.trim())){
            throw new IllegalArgumentException("Invalid expression");
        }
        expression = expression.trim();
        if(cellName == null || "".equals(cellName.trim())){
            throw new IllegalArgumentException("Invalid vertex");
        }else{
            String finalVertexName = cellName.trim().toLowerCase();
            if(!graph.isValidVertices(finalVertexName)){
                throw new IllegalArgumentException(String.format("%s is " +
                        "not a valid cell reference", cellName));
            }else{
                MathExpression.MathExpressionBuilder builder = new MathExpressionBuilder(expression);
                if(!this.builder.udf.isEmpty()){
                    builder.withUserDefineFunction(this.builder.udf.toArray(new AbstractFunction[this.builder.udf.size()]));
                }
                if(dependencyCellNames!=null && dependencyCellNames.length!=0){
                    for(final String dependencyVertex:dependencyCellNames){
                        if(dependencyVertex==null || "".equals(dependencyVertex.trim())){
                            throw new IllegalArgumentException("Invalid Dependency Vertex");
                        }else{
                            final String finalDependencyVertex = dependencyVertex.trim().toLowerCase();
                            if(!graph.isValidVertices(finalDependencyVertex)){
                                throw new IllegalArgumentException(String.format("%s is not a " +
                                        "valid cell reference", dependencyVertex));
                            }else{
                                graph.addEdge(finalVertexName,finalDependencyVertex);
                                builder.withVariableOrExpressionsNames(finalDependencyVertex);
                            }
                        }
                    }
                }
                mapExMap.put(finalVertexName,builder.build());
                return this;
            }
        }
    }


    /**
     * @author i.dey
     * Builder class to build {@link Excel} which can contains {@link  MathExpression} in its rows and col
     */
    public static final class ExcelBuilder{
        private final Graph<String> graph;
        private final Map<String, MathExpression> mapExMap;
        private final int rows;
        private final int cols;
        private List<AbstractFunction> udf;

        /**
         *
         * @param rows number of rows in excel
         * @param cols number of cols in excel
         * @param udf array of User Define function that excel can support
         * @throws IllegalArgumentException in case rows and cols are invalid
         */
        public ExcelBuilder(int rows, int cols, AbstractFunction... udf) {
            if(rows<=0 || cols<=0){
                throw new IllegalArgumentException("Invalid rows or col");
            }
            graph = new Graph<>();
            mapExMap = new LinkedHashMap<>();
            this.rows = rows;
            this.cols = cols;
            this.udf = Collections.emptyList();
            if(udf!=null && udf.length>0){
                this.udf = Arrays.asList(udf);
            }
            initialize();
        }

        /**
         * @param rows number of rows in excel
         * @param cols number of cols in excel
         * @throws IllegalArgumentException in case rows and cols are invalid
         */
        public ExcelBuilder(int rows, int cols){
            this(rows,cols,null);
        }

        /**
         * initialize the graph and mapExMap with DEFAULT_MATHEXPRESSION
         */
        private void initialize(){
            if(!mapExMap.isEmpty()){
                mapExMap.clear();
            }
            if(!graph.isEmpty()){
                graph.clear();
            }
            for(int row=1;row<=rows;row++){
                for(int col=1;col<=cols;col++){
                    String vertex = String.format(CELL_NAME,
                            PositiveBaseConverterEnum.EXCEL_ENCODING.encode(col),row).toLowerCase();
                    mapExMap.put(vertex, DEFAULT_MATHEXPRESSION);
                    graph.addVertex(vertex);
                }
            }
        }


        /**
         *
         * @return Excel
         */
        public Excel build(){
            return new Excel(graph,mapExMap, rows, cols, this);
        }
    }

}

package org.idey.excel.graph;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.idey.excel.rule.ExceptionLoggingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(JUnitParamsRunner.class)
public class GraphTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "checkSuccessAddVertex")
    public void testSucessAddVertex(Integer[] array){
        Graph<Integer> g = new Graph<>();
        for(Integer val:array){
            g.addVertex(val);
        }
        Assert.assertNotNull(g);
    }

    private Object[] checkSuccessAddVertex() {
        return new Object[]{
                new Object[]{new Integer[]{1,2,3}},
        };
    }

    @Test
    @Parameters(method = "checkFailedAddVertex")
    public void testFailedAddVertex(Integer[] array){
        expectedException.expect(IllegalArgumentException.class);
        Graph<Integer> g = new Graph<>();
        for(Integer val:array){
            g.addVertex(val);
        }
        Assert.assertNotNull(g);
    }

    private Object[] checkFailedAddVertex() {
        return new Object[]{
                new Object[]{new Integer[]{1,1,3}},
                new Object[]{new Integer[]{null}},
        };
    }

    @Test
    @Parameters(method = "checkSuccessAddEdge")
    public void testSucessAddEdgeTest(Set<Integer> vertexs, Map<Integer, Set<Integer>> mapPairOfVertex){
        Graph<Integer> g = new Graph<>();
        if(vertexs !=null && !vertexs.isEmpty()) {
            vertexs.forEach(g::addVertex);
        }
        mapPairOfVertex.entrySet().forEach(e->e.getValue()
                .forEach(val -> g.addEdge(e.getKey(),val)));
    }

    private Object[] checkSuccessAddEdge() {
        return new Object[]{
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4})

        };
    }


    @Test
    @Parameters(method = "checkFailedAddEdge")
    public void testFailedAddEdgeTest(Set<Integer> vertexs,
                                      Map<Integer, Set<Integer>> mapPairOfVertex){
        expectedException.expect(IllegalArgumentException.class);
        Graph<Integer> g = new Graph<>();
        if(vertexs !=null && !vertexs.isEmpty()) {
            vertexs.forEach(g::addVertex);
        }
        mapPairOfVertex.entrySet().forEach(e->e.getValue()
                .forEach(val -> g.addEdge(e.getKey(),val)));
    }

    private Object[] checkFailedAddEdge() {
        return new Object[]{
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{1,null}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{null,2}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{6,2}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{2,6}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{3,3}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{new Integer(3),3}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{3,new Integer(3)}),
                returnGraphWithVertexConnect(new Integer[]{1,2,3,4}, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{4,1}, new Integer[]{3,4})
        };
    }




    private Object[] returnGraphWithVertexConnect(Integer[] array, Integer[]... pairs){
        Object[] inputs = new Object[2];
        inputs[0] = new HashSet<>(Arrays.asList(array));
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for(Integer[] pair:pairs){
            Set<Integer> values = map.get(pair[0]);
            if(values == null){
                values = new HashSet<>();
                map.put(pair[0],values);
            }
            values.add(pair[1]);
        }
        inputs[1] = map;
        return inputs;
    }


    @Test
    @Parameters(method = "checkSuccessGetAllNeighbours")
    public void testSuccessGetAllNeighbours(Set<Integer> vertexs,
                                            Map<Integer, Set<Integer>> mapPairOfVertex,
                                            Integer vertex,Set<Integer> expectedNeighbours){

        Graph<Integer> g = new Graph<>();
        if(vertexs !=null && !vertexs.isEmpty()) {
            vertexs.forEach(g::addVertex);
        }
        mapPairOfVertex.entrySet().forEach(e->e.getValue()
                .forEach(val -> g.addEdge(e.getKey(),val)));
        Set<Integer> resultVertex = g.getAllNeighbours(vertex);
        Assert.assertEquals(resultVertex,expectedNeighbours);
    }


    private Object[] checkSuccessGetAllNeighbours(){
        return new Object[]{
                returnGraphWithVertexConnectWithAllNeighbours(new Integer[]{1,2,3,4},
                        1,new HashSet<>(Arrays.asList(2,3,4)),  new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4})
        };
    }


    @Test
    @Parameters(method = "checkFailedGetAllNeighbours")
    public void testFailedGetAllNeighbours(Set<Integer> vertexs,
                                            Map<Integer, Set<Integer>> mapPairOfVertex,
                                            Integer vertex,Set<Integer> expectedNeighbours){

        expectedException.expect(IllegalArgumentException.class);
        Graph<Integer> g = new Graph<>();
        if(vertexs !=null && !vertexs.isEmpty()) {
            vertexs.forEach(g::addVertex);
        }
        mapPairOfVertex.entrySet().forEach(e->e.getValue()
                .forEach(val -> g.addEdge(e.getKey(),val)));
        Set<Integer> resultVertex = g.getAllNeighbours(vertex);
    }


    private Object[] checkFailedGetAllNeighbours(){
        return new Object[]{
                returnGraphWithVertexConnectWithAllNeighbours(new Integer[]{1,2,3,4},
                        null,new HashSet<>(Arrays.asList(2,3,4)),  new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4}),
                returnGraphWithVertexConnectWithAllNeighbours(new Integer[]{1,2,3,4},
                        6,new HashSet<>(Arrays.asList(2,3,4)),  new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4})
        };
    }


    private Object[] returnGraphWithVertexConnectWithAllNeighbours(Integer[] array, Integer vertex, Set<Integer> expectedNeighbours, Integer[]... pairs){
        Object[] inputs = new Object[4];
        Object[] output = returnGraphWithVertexConnect(array,pairs);
        inputs[0] = output[0];
        inputs[1] = output[1];
        inputs[2] = vertex;
        inputs[3] = expectedNeighbours;
        System.out.println(Arrays.deepToString(inputs));
        return inputs;
    }



   

}

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
import java.util.Collections;
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
            Set<Integer> values = map.computeIfAbsent(pair[0], k -> new HashSet<>());
            values.add(pair[1]);
        }
        inputs[1] = map;
        return inputs;
    }


    @Test
    @Parameters(method = "checkSuccessGetAllOrImmediateNeighbours")
    public void testSuccessGetAllOrImmediateNeighbours(Set<Integer> vertexs,
                                                       Map<Integer, Set<Integer>> mapPairOfVertex,
                                                       Integer vertex, Set<Integer> expectedNeighbours, boolean isAll){

        Graph<Integer> g = new Graph<>();
        if(vertexs !=null && !vertexs.isEmpty()) {
            vertexs.forEach(g::addVertex);
        }
        mapPairOfVertex.entrySet().forEach(e->e.getValue()
                .forEach(val -> g.addEdge(e.getKey(),val)));
        Set<Integer> resultVertex;
        if(isAll)
            resultVertex = g.getAllNeighbours(vertex);
        else
            resultVertex = g.getAllImediateNeighbours(vertex);
        Assert.assertEquals(resultVertex,expectedNeighbours);
    }


    private Object[] checkSuccessGetAllOrImmediateNeighbours(){
        return new Object[]{
                returnGraphWithVertexConnectWithAllOrImmediateNeighbours(new Integer[]{1,2,3,4},
                        1,new HashSet<>(Arrays.asList(2,3,4)), true, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4}),
                returnGraphWithVertexConnectWithAllOrImmediateNeighbours(new Integer[]{1,2,3,4},
                        1,new HashSet<>(Arrays.asList(2,3)), false, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4})
        };
    }


    @Test
    @Parameters(method = "checkFailedGetAllNeighbours")
    public void testFailedGetAllOrImmediateNeighbours(Set<Integer> vertexs,
                                                      Map<Integer, Set<Integer>> mapPairOfVertex,
                                                      Integer vertex, Set<Integer> expectedNeighbours, boolean isAll){

        expectedException.expect(IllegalArgumentException.class);
        Graph<Integer> g = new Graph<>();
        if(vertexs !=null && !vertexs.isEmpty()) {
            vertexs.forEach(g::addVertex);
        }
        mapPairOfVertex.entrySet().forEach(e->e.getValue()
                .forEach(val -> g.addEdge(e.getKey(),val)));
        Set<Integer> resultVertex = isAll ? g.getAllNeighbours(vertex) : g.getAllImediateNeighbours(vertex);
    }


    private Object[] checkFailedGetAllNeighbours(){
        return new Object[]{
                returnGraphWithVertexConnectWithAllOrImmediateNeighbours(new Integer[]{1,2,3,4},
                        null,new HashSet<>(Arrays.asList(2,3,4)),  true, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4}),
                returnGraphWithVertexConnectWithAllOrImmediateNeighbours(new Integer[]{1,2,3,4},
                        6,new HashSet<>(Arrays.asList(2,3,4)),  true, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4}),
                returnGraphWithVertexConnectWithAllOrImmediateNeighbours(new Integer[]{1,2,3,4},
                        null,new HashSet<>(Arrays.asList(2,3,4)),  false, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4}),
                returnGraphWithVertexConnectWithAllOrImmediateNeighbours(new Integer[]{1,2,3,4},
                        6,new HashSet<>(Arrays.asList(2,3,4)),  false, new Integer[]{1,2},
                        new Integer[]{2,3},new Integer[]{1,3}, new Integer[]{3,4})
        };
    }


    private Object[] returnGraphWithVertexConnectWithAllOrImmediateNeighbours(Integer[] array, Integer vertex, Set<Integer> expectedNeighbours,
                                                                              boolean isAll, Integer[]... pairs){
        Object[] inputs = new Object[5];
        Object[] output = returnGraphWithVertexConnect(array,pairs);
        inputs[0] = output[0];
        inputs[1] = output[1];
        inputs[2] = vertex;
        inputs[3] = expectedNeighbours;
        inputs[4] = isAll;
        return inputs;
    }

    @Test
    @Parameters(method = "checkGetAllVertices")
    public void testGetAllVertex(Integer[] vertices, Set<Integer> expectedVertices){
        Graph<Integer> g = new Graph<>();
        if(vertices!=null && vertices.length>0){
            for(Integer val:vertices){
                g.addVertex(val);
            }
        }
        Assert.assertEquals(g.getAllVertices(),expectedVertices);
    }


    private Object[] checkGetAllVertices() {
        return new Object[]{
                new Object[]{new Integer[]{1,2,3}, new HashSet<>(Arrays.asList(1,2,3))},
                new Object[]{new Integer[]{}, Collections.emptySet()},
                new Object[]{null, Collections.emptySet()}
        };
    }

    @Test
    @Parameters(method = "checkIsValidVertex")
    public void testIsValidVertex(Integer[] vertices, Integer vertex, boolean expectedResult){
        Graph<Integer> g = new Graph<>();
        if(vertices!=null && vertices.length>0){
            for(Integer val:vertices){
                g.addVertex(val);
            }
        }
        Assert.assertEquals(g.isValidVertices(vertex),expectedResult);
    }


    private Object[] checkIsValidVertex() {
        return new Object[]{
                new Object[]{new Integer[]{1,2,3}, 2, true},
                new Object[]{new Integer[]{1,2,3}, 4, false},
                new Object[]{new Integer[]{}, 1, false},
                new Object[]{null, 1, false}
        };
    }
   

}

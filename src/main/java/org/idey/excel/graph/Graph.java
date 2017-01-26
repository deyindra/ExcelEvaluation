package org.idey.excel.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author i.dey
 * A directed graph data structure
 * @param <T>
 */
public final class Graph<T> {
    private Map<T,Set<T>> map;

    public Graph() {
        map = new HashMap<>();
    }

    /**
     *
     * @param obj Vertex
     * @return Graph
     * @throws IllegalArgumentException in case vertex is null or already exists in the graph
     */
    public Graph<T> addVertex(T obj){
        if(obj==null){
            throw new IllegalArgumentException("Vertex can not be null");
        }else{
            if(map.containsKey(obj)){
                throw new IllegalArgumentException(String.format("%s is already exists", obj.toString()));
            }else{
                map.put(obj, new LinkedHashSet<>());
                return this;
            }
        }
    }

    /**
     * Add edge between source and destination vertex, it also check the circular dependency
     * @param source Sourc Vertex
     * @param desitination Vertex
     * @return Graph
     * @throws IllegalArgumentException in case source and desitination does not exists and or cycle detected
     */
    public Graph<T> addEdge(T source, T desitination){
        if(source==null || desitination==null){
            throw new IllegalArgumentException("Invalid nodes");
        }else{
            if(!map.containsKey(source) || !map.containsKey(desitination)){
                throw new IllegalArgumentException(String.format("Either %s or %s is not vertex of graph",
                        source.toString(), desitination.toString()));
            }else{
                if(source==desitination || source.equals(desitination)){
                    throw new IllegalArgumentException("Cycle Detected...");
                }else{
                    Set<T> visted = new HashSet<>();
                    Queue<T> queue = new LinkedList<>();
                    visted.add(desitination);
                    queue.add(desitination);
                    while (!queue.isEmpty()) {
                        T vertex = queue.poll();
                        Set<T> neighbours = map.get(vertex);
                        for (T neighbour : neighbours) {
                            if (neighbour == source || neighbour.equals(source)) {
                                throw new IllegalArgumentException(String.format("Cycle Detected, " +
                                                "if we connect vertext from %s to %s", source.toString(),
                                        desitination.toString()));
                            }
                            if(!visted.contains(neighbour)){
                                visted.add(neighbour);
                                queue.add(neighbour);
                            }
                        }
                    }
                    map.get(source).add(desitination);
                    return this;
                }
            }
        }
    }

    /**
     *
     * @param vertex Source Vertex
     * @return get All dependant vertex
     * @throws IllegalArgumentException in case vertex is null or not found
     */
    public Set<T> getAllNeighbours(T vertex){
        if(vertex == null){
            throw new IllegalArgumentException("Invalid vertex");
        }else{
            if(!map.containsKey(vertex)){
                throw new IllegalArgumentException(String.format("%s is not found", vertex.toString()));
            }else{
                Set<T> visited = new HashSet<>();
                Set<T> childs = new HashSet<>();
                fetchAllNeighbours(vertex,visited,childs);
                return Collections.unmodifiableSet(childs);
            }
        }
    }

    /**
     *
     * @param vertex Source Vertex
     * @return get Immediate dependant vertex
     * @throws IllegalArgumentException in case vertex is null or not found
     */
    public Set<T> getAllImediateNeighbours(T vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Invalid vertex");
        } else {
            if (!map.containsKey(vertex)) {
                throw new IllegalArgumentException(String.format("%s is not found", vertex.toString()));
            } else {
                return Collections.unmodifiableSet(map.get(vertex));
            }
        }
    }


    private void fetchAllNeighbours(T vertex, Set<T> visisted, Set<T> childs){
        map.get(vertex).stream().filter(child -> !visisted.contains(child)).forEach(child -> {
            visisted.add(child);
            childs.add(child);
            fetchAllNeighbours(child,visisted,childs);
        });
    }

    /**
     *
     * @return all vertex
     */
    public Set<T> getAllVertices(){
        return Collections.unmodifiableSet(map.keySet());
    }

    /**
     *
     * @param obj vertex
     * @return true or false based on vertex is valid or not
     */
    public boolean isValidVertices(T obj){
        return map.containsKey(obj);
    }

    /**
     * Clear the graph
     */
    public void clear(){
        map.clear();
    }

    /**
     *
     * @return true if graph does not have any vertex
     */
    public boolean isEmpty(){
        return map.isEmpty();
    }
}

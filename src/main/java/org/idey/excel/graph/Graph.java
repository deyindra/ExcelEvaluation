package org.idey.excel.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph<T> {
    private Map<T,Set<T>> map;

    public Graph() {
        map = new HashMap<>();
    }

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

    public Set<T> getAllVertices(){
        return Collections.unmodifiableSet(map.keySet());
    }

    public boolean isValidVertices(T obj){
        return map.containsKey(obj);
    }
}

package model.state;

import java.util.List;
import java.util.Map;

public interface BarrierTable {
    int add(int value, List<Integer> list);
    Map.Entry<Integer, List<Integer>> get(int key);
    boolean isDefined(int key);
    void update(int key, int value, List<Integer> list);
    Map<Integer, Map.Entry<Integer, List<Integer>>> getContent();
    void setContent(Map<Integer, Map.Entry<Integer, List<Integer>>> content);
    String toString();
}
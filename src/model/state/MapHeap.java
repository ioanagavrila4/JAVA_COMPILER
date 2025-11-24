package model.state;

import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MapHeap implements Heap {
    private Map<Integer, Value> heap;
    private int freeAddress;

    public MapHeap() {
        this.heap = new HashMap<>();
        this.freeAddress = 1; // Addresses start from 1, 0 is invalid (null)
    }

    @Override
    public int allocate(Value value) {
        int address = freeAddress;
        heap.put(address, value);
        freeAddress++;
        return address;
    }

    @Override
    public Value get(int address) {
        return heap.get(address);
    }

    @Override
    public void update(int address, Value value) {
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        this.heap = newContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Heap:\n");
        if (heap.isEmpty()) {
            sb.append("  (empty)\n");
        } else {
            for (Map.Entry<Integer, Value> entry : heap.entrySet()) {
                sb.append("  ").append(entry.getKey())
                  .append(" -> ").append(entry.getValue())
                  .append("\n");
            }
        }
        return sb.toString();
    }
}

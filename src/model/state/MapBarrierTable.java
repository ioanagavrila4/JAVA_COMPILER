package model.state;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapBarrierTable implements BarrierTable {
    private Map<Integer, Map.Entry<Integer, List<Integer>>> barrierTable;
    private int freeLocation;
    private final Lock lock;

    public MapBarrierTable() {
        this.barrierTable = new HashMap<>();
        this.freeLocation = 0;
        this.lock = new ReentrantLock();
    }

    @Override
    public int add(int value, List<Integer> list) {
        lock.lock();
        try {
            freeLocation++;
            barrierTable.put(freeLocation, new AbstractMap.SimpleEntry<>(value, list));
            return freeLocation;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map.Entry<Integer, List<Integer>> get(int key) {
        lock.lock();
        try {
            return barrierTable.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isDefined(int key) {
        lock.lock();
        try {
            return barrierTable.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int key, int value, List<Integer> list) {
        lock.lock();
        try {
            barrierTable.put(key, new AbstractMap.SimpleEntry<>(value, list));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Map.Entry<Integer, List<Integer>>> getContent() {
        lock.lock();
        try {
            return new HashMap<>(barrierTable);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setContent(Map<Integer, Map.Entry<Integer, List<Integer>>> content) {
        lock.lock();
        try {
            this.barrierTable = content;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Integer, Map.Entry<Integer, List<Integer>>> entry : barrierTable.entrySet()) {
                builder.append(entry.getKey())
                      .append(" -> (")
                      .append(entry.getValue().getKey())
                      .append(", ")
                      .append(entry.getValue().getValue())
                      .append(")\n");
            }
            return builder.toString();
        } finally {
            lock.unlock();
        }
    }
}
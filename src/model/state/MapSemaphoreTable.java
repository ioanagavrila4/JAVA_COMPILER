package model.state;

import javafx.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe implementation of SemaphoreTable using HashMap
 * Uses ReadWriteLock for efficient concurrent access
 */
public class MapSemaphoreTable implements SemaphoreTable {
    private final Map<Integer, Pair<Integer, List<Integer>>> semaphoreMap;
    private final ReadWriteLock lock;
    private int nextFreeLocation;

    public MapSemaphoreTable() {
        this.semaphoreMap = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.nextFreeLocation = 1; // Start from 1, 0 is reserved for null/invalid
    }

    @Override
    public int createSemaphore(int initialCount) {
        lock.writeLock().lock();
        try {
            int location = nextFreeLocation++;
            semaphoreMap.put(location, new Pair<>(initialCount, new java.util.ArrayList<>()));
            return location;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Pair<Integer, List<Integer>> getSemaphore(int index) {
        lock.readLock().lock();
        try {
            return semaphoreMap.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void updateSemaphore(int index, int count, List<Integer> waitingList) {
        lock.writeLock().lock();
        try {
            semaphoreMap.put(index, new Pair<>(count, waitingList));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsSemaphore(int index) {
        lock.readLock().lock();
        try {
            return semaphoreMap.containsKey(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        lock.readLock().lock();
        try {
            return new HashMap<>(semaphoreMap);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setContent(Map<Integer, Pair<Integer, List<Integer>>> content) {
        lock.writeLock().lock();
        try {
            semaphoreMap.clear();
            semaphoreMap.putAll(content);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SemaphoreTable:\n");
            for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : semaphoreMap.entrySet()) {
                builder.append("  ")
                       .append(entry.getKey())
                       .append(" -> (count: ")
                       .append(entry.getValue().getKey())
                       .append(", waiting: ")
                       .append(entry.getValue().getValue())
                       .append(")\n");
            }
            return builder.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
}
package model.state;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe implementation of LatchTable using ReentrantReadWriteLock
 * for atomic operations as required by the exam
 */
public class MapLatchTable implements LatchTable {
    private Map<Integer, Integer> latchTable;
    private int freeLocation;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public MapLatchTable() {
        this.latchTable = new HashMap<>();
        this.freeLocation = 1; // Locations start from 1
    }

    @Override
    public synchronized int allocate(int count) {
        lock.writeLock().lock();
        try {
            int location = freeLocation;
            latchTable.put(location, count);
            freeLocation++;
            return location;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int get(int location) {
        lock.readLock().lock();
        try {
            Integer value = latchTable.get(location);
            return value != null ? value : -1; // Return -1 if not found
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void update(int location, int count) {
        lock.writeLock().lock();
        try {
            latchTable.put(location, count);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isDefined(int location) {
        lock.readLock().lock();
        try {
            return latchTable.containsKey(location);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<Integer, Integer> getContent() {
        lock.readLock().lock();
        try {
            return new HashMap<>(latchTable);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setContent(Map<Integer, Integer> newContent) {
        lock.writeLock().lock();
        try {
            this.latchTable = new HashMap<>(newContent);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean countDown(int location) {
        lock.writeLock().lock();
        try {
            if (!latchTable.containsKey(location)) {
                return false;
            }
            int currentCount = latchTable.get(location);
            if (currentCount > 0) {
                latchTable.put(location, currentCount - 1);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("LatchTable:\n");
            if (latchTable.isEmpty()) {
                sb.append("  (empty)\n");
            } else {
                for (Map.Entry<Integer, Integer> entry : latchTable.entrySet()) {
                    sb.append("  ").append(entry.getKey())
                      .append(" -> ").append(entry.getValue())
                      .append("\n");
                }
            }
            return sb.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
}
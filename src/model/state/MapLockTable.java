package model.state;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MapLockTable implements LockTable {
    private Map<Integer, Integer> lockTable;
    private int nextFreeLocation;
    private final ReentrantReadWriteLock lock;

    public MapLockTable() {
        this.lockTable = new HashMap<>();
        this.nextFreeLocation = 1; // Start from 1, 0 is reserved for null
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public int allocateLock() {
        lock.writeLock().lock();
        try {
            int location = nextFreeLocation;
            lockTable.put(location, -1); // -1 means the lock is free
            nextFreeLocation++;
            return location;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int get(int location) {
        lock.readLock().lock();
        try {
            return lockTable.getOrDefault(location, -1);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void update(int location, int value) {
        lock.writeLock().lock();
        try {
            if (lockTable.containsKey(location)) {
                lockTable.put(location, value);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isDefined(int location) {
        lock.readLock().lock();
        try {
            return lockTable.containsKey(location);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<Integer, Integer> getContent() {
        lock.readLock().lock();
        try {
            return new HashMap<>(lockTable);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setContent(Map<Integer, Integer> content) {
        lock.writeLock().lock();
        try {
            this.lockTable = new HashMap<>(content);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, Integer> entry : lockTable.entrySet()) {
                sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
            }
            return sb.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
}
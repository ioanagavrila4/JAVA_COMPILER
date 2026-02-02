package model.state;

import java.util.Map;

public interface LockTable {
    // Allocate a new lock and return its location
    int allocateLock();

    // Get the value at a specific location (-1 if free, thread ID if locked)
    int get(int location);

    // Update a lock location with a new value (thread ID or -1)
    void update(int location, int value);

    // Check if a location exists in the table
    boolean isDefined(int location);

    // Get the entire content of the lock table
    Map<Integer, Integer> getContent();

    // Set the entire content of the lock table
    void setContent(Map<Integer, Integer> content);
}
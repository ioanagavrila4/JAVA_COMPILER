package model.state;

import java.util.Map;

/**
 * Interface for the LatchTable - a global table shared among different threads
 * that maps an integer to an integer for CountDownLatch implementation
 */
public interface LatchTable {
    /**
     * Allocates a new latch entry and returns the location
     * @param count The initial count value for the latch
     * @return The location where the latch was stored
     */
    int allocate(int count);

    /**
     * Retrieves the count value from the latch table at the given location
     * @param location The location to read from
     * @return The count value stored at that location
     */
    int get(int location);

    /**
     * Updates the latch table at the given location with a new count value
     * @param location The location to update
     * @param count The new count value to store
     */
    void update(int location, int count);

    /**
     * Checks if a location exists in the latch table
     * @param location The location to check
     * @return true if the location is defined, false otherwise
     */
    boolean isDefined(int location);

    /**
     * Gets the entire latch table content
     * @return A map of locations to count values
     */
    Map<Integer, Integer> getContent();

    /**
     * Sets the entire latch table content
     * @param newContent The new latch table content
     */
    void setContent(Map<Integer, Integer> newContent);

    /**
     * Decrements the count at the given location if greater than 0
     * @param location The location to decrement
     * @return true if the count was decremented, false if it was already 0
     */
    boolean countDown(int location);
}
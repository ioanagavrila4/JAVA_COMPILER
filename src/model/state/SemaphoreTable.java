package model.state;

import javafx.util.Pair;
import java.util.List;
import java.util.Map;

/**
 * Interface for SemaphoreTable - manages counting semaphores
 * Maps integer keys to pairs of (counter, list of waiting program IDs)
 * All operations must be thread-safe (synchronized)
 */
public interface SemaphoreTable {
    /**
     * Allocates a new semaphore with the given initial count
     * @param initialCount the initial count for the semaphore
     * @return the unique identifier for the new semaphore
     */
    int createSemaphore(int initialCount);

    /**
     * Gets the semaphore data (count and waiting list)
     * @param index the semaphore identifier
     * @return pair of (count, list of waiting program IDs) or null if not found
     */
    Pair<Integer, List<Integer>> getSemaphore(int index);

    /**
     * Updates the semaphore data
     * @param index the semaphore identifier
     * @param count the new count value
     * @param waitingList the new waiting list
     */
    void updateSemaphore(int index, int count, List<Integer> waitingList);

    /**
     * Checks if a semaphore exists
     * @param index the semaphore identifier
     * @return true if the semaphore exists, false otherwise
     */
    boolean containsSemaphore(int index);

    /**
     * Gets all semaphore data for display
     * @return map of all semaphores
     */
    Map<Integer, Pair<Integer, List<Integer>>> getContent();

    /**
     * Sets the entire content of the semaphore table
     * @param content the new content
     */
    void setContent(Map<Integer, Pair<Integer, List<Integer>>> content);

    /**
     * Creates a string representation of the semaphore table
     * @return string representation
     */
    String toString();
}
package model.state;

import model.value.Value;

import java.util.Map;

public interface Heap {
    /**
     * Allocates a new heap entry and returns the address
     * @param value The value to store in the heap
     * @return The address where the value was stored
     */
    int allocate(Value value);

    /**
     * Retrieves a value from the heap at the given address
     * @param address The address to read from
     * @return The value stored at that address
     */
    Value get(int address);

    /**
     * Updates the heap at the given address with a new value
     * @param address The address to update
     * @param value The new value to store
     */
    void update(int address, Value value);

    /**
     * Checks if an address exists in the heap
     * @param address The address to check
     * @return true if the address is defined, false otherwise
     */
    boolean isDefined(int address);

    /**
     * Gets the entire heap content (for garbage collection)
     * @return A map of addresses to values
     */
    Map<Integer, Value> getContent();

    /**
     * Sets the entire heap content (for garbage collection)
     * @param newContent The new heap content
     */
    void setContent(Map<Integer, Value> newContent);
}

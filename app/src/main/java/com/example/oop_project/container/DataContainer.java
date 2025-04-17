package com.example.oop_project.container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generic container class for storing and filtering data items.
 *
 * @param <T> The type of elements stored in this container
 */
public class DataContainer<T> {
    // Internal data storage
    private final List<T> data;

    /**
     * Constructs an empty DataContainer
     */
    public DataContainer() {
        this.data = new ArrayList<>();
    }

    /**
     * Adds an element to the container
     * @param element The element to add
     */
    public void addData(T element) {
        data.add(element);
    }

    /**
     * Gets a copy of all items in the container
     * @return New List containing all elements
     */
    public List<T> getAllItems() {
        return new ArrayList<>(data);
    }

    /**
     * Filters the container using the specified predicate
     * @param predicate The filtering condition
     * @return New DataContainer containing only matching elements
     */
    public DataContainer<T> filter(Predicate<T> predicate) {
        DataContainer<T> filteredContainer = new DataContainer<>();

        // Use Java Streams to filter and collect matching items
        List<T> filteredItems = data.stream()
                .filter(predicate)
                .collect(Collectors.toList());

        // Add filtered items to new container
        filteredItems.forEach(filteredContainer::addData);

        return filteredContainer;
    }
}
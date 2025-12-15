package model.utils;

import exceptions.MyException;

public interface MyIDictionary<K, V> {
    void add(K key, V value) throws MyException;
    V lookup(K key) throws MyException;
    boolean isDefined(K key);
    void update(K key, V value) throws MyException;
    MyIDictionary<K, V> clone();
}
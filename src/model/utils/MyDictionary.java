package model.utils;

import exceptions.MyException;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private Map<K, V> dictionary;

    public MyDictionary() {
        this.dictionary = new HashMap<>();
    }

    @Override
    public void add(K key, V value) throws MyException {
        dictionary.put(key, value);
    }

    @Override
    public V lookup(K key) throws MyException {
        V value = dictionary.get(key);
        if (value == null) {
            throw new MyException("Variable '" + key + "' is not defined in the type environment");
        }
        return value;
    }

    @Override
    public boolean isDefined(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public void update(K key, V value) throws MyException {
        if (!isDefined(key)) {
            throw new MyException("Variable '" + key + "' is not defined in the type environment");
        }
        dictionary.put(key, value);
    }

    @Override
    public MyIDictionary<K, V> clone() {
        MyDictionary<K, V> newDict = new MyDictionary<>();
        newDict.dictionary = new HashMap<>(this.dictionary);
        return newDict;
    }

    @Override
    public String toString() {
        return dictionary.toString();
    }
}
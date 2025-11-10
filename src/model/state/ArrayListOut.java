package model.state;

import model.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOut implements Out {
    private final List<Value> values = new ArrayList<>();

    @Override
    public void add(Value value) {
        values.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Value value : values) {
            sb.append(value.toString()).append("\n");
        }
        return sb.toString();
    }
}

package model.value;

import model.type.IntegerType;
import model.type.Type;

public record IntValue(int value) implements Value {

    @Override
    public Type getType() {
        return new IntegerType();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntValue other) {
            return this.value == other.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

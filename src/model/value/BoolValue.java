package model.value;

import model.type.BooleanType;
import model.type.Type;

public record BoolValue(boolean value) implements Value {

    @Override
    public Type getType() {
        return new BooleanType();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoolValue other) {
            return this.value == other.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

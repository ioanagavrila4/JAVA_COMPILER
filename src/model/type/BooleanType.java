package model.type;

import model.value.BoolValue;
import model.value.Value;

public class BooleanType extends Type {
    @Override
    public Value getDefaultValue() {
        return new BoolValue(false);
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BooleanType;
    }

    @Override
    public int hashCode() {
        return 2;
    }

    @Override
    public String toString() {
        return "bool";
    }
}

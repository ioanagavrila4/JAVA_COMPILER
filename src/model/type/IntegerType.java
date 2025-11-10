package model.type;

import model.value.IntValue;
import model.value.Value;

public class IntegerType extends Type {
    @Override
    public Value getDefaultValue() {
        return new IntValue(0);
    }

    @Override
    public Value defaultValue() {
        return new IntValue(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntegerType;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "int";
    }
}

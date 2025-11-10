package model.type;

import model.value.StringValue;
import model.value.Value;

public class StringType extends Type {
    @Override
    public Value getDefaultValue() {
        return new StringValue("");
    }

    @Override
    public Value defaultValue() {
        return new StringValue("");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }

    @Override
    public int hashCode() {
        return 3;
    }

    @Override
    public String toString() {
        return "string";
    }
}

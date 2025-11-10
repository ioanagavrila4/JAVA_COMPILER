package model.type;

import model.value.Value;

public abstract class Type {
    public abstract Value getDefaultValue();

    public abstract Value defaultValue();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

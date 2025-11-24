package model.value;

import model.type.RefType;
import model.type.Type;

public record RefValue(int address, Type locationType) implements Value {

    public int getAddr() {
        return address;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }
}

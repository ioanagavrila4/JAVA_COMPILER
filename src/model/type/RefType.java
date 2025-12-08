package model.type;

import model.value.RefValue;
import model.value.Value;

public class RefType extends Type {
    private final Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public Value getDefaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public Value defaultValue() {
        return new RefValue(0, inner);
    }
//de rescris functia cu dublu if fara o variabila noua

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefType refType) {
            return inner.equals(refType.getInner());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 4 + inner.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }
}

package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.RefType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.RefValue;
import model.value.Value;

public record ReadHeapExpression(Expression expression) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value value = expression.evaluate(symbolTable, heap);

        if (!(value instanceof RefValue refValue)) {
            throw new RuntimeException("ReadHeapExpression: expression is not a RefValue");
        }

        int address = refValue.getAddr();

        if (!heap.isDefined(address)) {
            throw new RuntimeException("ReadHeapExpression: address " + address + " is not defined in heap");
        }

        return heap.get(address);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else {
            throw new MyException("the rH argument is not a Ref Type");
        }
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}

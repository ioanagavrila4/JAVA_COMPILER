package model.expression;

import model.state.Heap;
import model.state.SymbolTable;
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
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}

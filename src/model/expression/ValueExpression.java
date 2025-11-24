package model.expression;

import model.state.Heap;
import model.state.SymbolTable;
import model.value.Value;

public record ValueExpression(Value value) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

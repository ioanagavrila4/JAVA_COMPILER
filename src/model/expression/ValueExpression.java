package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.Value;

public record ValueExpression(Value value) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        return value;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return value.getType();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

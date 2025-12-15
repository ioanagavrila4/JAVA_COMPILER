package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.Value;

public record VariableExpression(String varName) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        if (!symbolTable.isDefined(varName))
            throw new RuntimeException("Variable " + varName + " is not defined");
        return symbolTable.getVariableValue(varName);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(varName);
    }

    @Override
    public String toString() {
        return varName;
    }
}

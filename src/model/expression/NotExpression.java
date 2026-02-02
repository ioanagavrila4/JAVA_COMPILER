package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.BooleanType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.Value;

public class NotExpression implements Expression {
    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value value = expression.evaluate(symbolTable, heap);
        if (!(value instanceof BoolValue boolValue)) {
            throw new ArithmeticException("NotExpression: operand is not a boolean");
        }
        return new BoolValue(!boolValue.value());
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type = expression.typecheck(typeEnv);
        if (!type.equals(new BooleanType())) {
            throw new MyException("NotExpression: operand is not a boolean");
        }
        return new BooleanType();
    }

    @Override
    public String toString() {
        return "!(" + expression.toString() + ")";
    }
}
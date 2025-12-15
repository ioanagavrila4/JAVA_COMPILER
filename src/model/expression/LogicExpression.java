package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.BooleanType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.Value;

public record LogicExpression(
        Expression first, Expression second,
        String operator) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue = first.evaluate(symbolTable, heap);
        Value rightValue = second.evaluate(symbolTable, heap);
        if (!(leftValue instanceof BoolValue(boolean leftTerm))) {
            throw new ArithmeticException("Not a bool");
        }
        if (!(rightValue instanceof BoolValue(boolean rightTerm))) {
            throw new ArithmeticException("Not a bool");
        }

        return switch (operator) {
            case "&&" -> new BoolValue(leftTerm && rightTerm);
            case "||" -> new BoolValue(leftTerm || rightTerm);
            default -> throw new ArithmeticException("Unknown operator");
        };
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = first.typecheck(typeEnv);
        typ2 = second.typecheck(typeEnv);

        if (typ1.equals(new BooleanType())) {
            if (typ2.equals(new BooleanType())) {
                return new BooleanType();
            } else {
                throw new MyException("second operand is not a boolean");
            }
        } else {
            throw new MyException("first operand is not a boolean");
        }
    }

    @Override
    public String toString() {
        return "(" + first.toString() + operator + second.toString() + ")";
    }
}

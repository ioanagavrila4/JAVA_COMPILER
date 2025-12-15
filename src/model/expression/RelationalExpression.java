package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;

public record RelationalExpression(
        Expression left, Expression right, String operator)
        implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue = left.evaluate(symbolTable, heap);
        Value rightValue = right.evaluate(symbolTable, heap);

        if (!(leftValue instanceof IntValue(int leftTerm))) {
            throw new ArithmeticException("RelationalExpression: left operand is not an integer");
        }
        if (!(rightValue instanceof IntValue(int rightTerm))) {
            throw new ArithmeticException("RelationalExpression: right operand is not an integer");
        }

        return switch (operator) {
            case "<" -> new BoolValue(leftTerm < rightTerm);
            case "<=" -> new BoolValue(leftTerm <= rightTerm);
            case "==" -> new BoolValue(leftTerm == rightTerm);
            case "!=" -> new BoolValue(leftTerm != rightTerm);
            case ">" -> new BoolValue(leftTerm > rightTerm);
            case ">=" -> new BoolValue(leftTerm >= rightTerm);
            default -> throw new ArithmeticException("RelationalExpression: unknown operator " + operator);
        };
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = left.typecheck(typeEnv);
        typ2 = right.typecheck(typeEnv);

        if (typ1.equals(new IntegerType())) {
            if (typ2.equals(new IntegerType())) {
                return new BooleanType();
            } else {
                throw new MyException("RelationalExpression: second operand is not an integer");
            }
        } else {
            throw new MyException("RelationalExpression: first operand is not an integer");
        }
    }

    @Override
    public String toString() {
        return "(" + left.toString() + operator + right.toString() + ")";
    }
}

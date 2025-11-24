package model.expression;

import model.state.Heap;
import model.state.SymbolTable;
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
    public String toString() {
        return "(" + left.toString() + operator + right.toString() + ")";
    }
}

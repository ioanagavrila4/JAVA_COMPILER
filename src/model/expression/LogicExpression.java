package model.expression;

import model.state.SymbolTable;
import model.value.BoolValue;
import model.value.Value;

public record LogicExpression(
        Expression first, Expression second,
        String operator) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        Value leftValue = first.evaluate(symbolTable);
        Value rightValue = second.evaluate(symbolTable);
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
    public String toString() {
        return "(" + first.toString() + operator + second.toString() + ")";
    }
}

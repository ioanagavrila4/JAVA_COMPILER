package model.expression;

import model.state.SymbolTable;
import model.value.IntValue;
import model.value.Value;

public record ArithmeticExpression(
        Expression left, Expression right, char operator)
        implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        Value leftValue = left.evaluate(symbolTable);
        Value rightValue = right.evaluate(symbolTable);
        if (!(leftValue instanceof IntValue(int leftTerm))) {
            throw new ArithmeticException("Not an integer");
        }
        if (!(rightValue instanceof IntValue(int rightTerm))) {
            throw new ArithmeticException("Not an integer");
        }

        return switch (operator) {
            case '+' -> new IntValue(leftTerm + rightTerm);
            case '-' -> new IntValue(leftTerm - rightTerm);
            case '*' -> new IntValue(leftTerm * rightTerm);
            case '/' -> divide(leftTerm, rightTerm);
            default -> throw new ArithmeticException("Unknown operator");
        };
    }

    private static IntValue divide(int leftTerm, int rightTerm) {
        if (rightTerm == 0)
            throw new ArithmeticException("Cannot divide by zero");
        return new IntValue(leftTerm / rightTerm);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + operator + right.toString() + ")";
    }
}

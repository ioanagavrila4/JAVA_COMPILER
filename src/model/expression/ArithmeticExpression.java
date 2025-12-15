package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

public record ArithmeticExpression(
        Expression left, Expression right, char operator)
        implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue = left.evaluate(symbolTable, heap);
        Value rightValue = right.evaluate(symbolTable, heap);
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

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = left.typecheck(typeEnv);
        typ2 = right.typecheck(typeEnv);

        if (typ1.equals(new IntegerType())) {
            if (typ2.equals(new IntegerType())) {
                return new IntegerType();
            } else {
                throw new MyException("second operand is not an integer");
            }
        } else {
            throw new MyException("first operand is not an integer");
        }
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

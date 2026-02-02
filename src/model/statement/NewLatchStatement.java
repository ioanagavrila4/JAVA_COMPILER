package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

/**
 * NewLatchStatement: newLatch(var, exp)
 * Creates a new countdown latch into the LatchTable
 */
public record NewLatchStatement(String variableName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Evaluate the expression exp using SymTable and Heap
        Value value = expression.evaluate(state.symbolTable(), state.heap());

        // Check if the result is an integer
        if (!value.getType().equals(new IntegerType())) {
            throw new RuntimeException("NewLatch: expression is not an integer");
        }

        IntValue intValue = (IntValue) value;
        int count = intValue.value();

        // Check if var exists in SymTable and has type int
        if (!state.symbolTable().isDefined(variableName)) {
            throw new RuntimeException("NewLatch: variable " + variableName + " is not defined");
        }

        Type varType = state.symbolTable().getVariableType(variableName);
        if (!varType.equals(new IntegerType())) {
            throw new RuntimeException("NewLatch: variable " + variableName + " is not of type int");
        }

        // Add the new latch to the LatchTable
        int newLocation = state.latchTable().allocate(count);

        // Update the variable in SymTable with the new location
        state.symbolTable().setValue(variableName, new IntValue(newLocation));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Verify if both var and exp have the type int
        Type varType = typeEnv.lookup(variableName);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("NewLatch: variable " + variableName + " is not of type int");
        }

        Type expType = expression.typecheck(typeEnv);
        if (!expType.equals(new IntegerType())) {
            throw new MyException("NewLatch: expression is not of type int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLatch(" + variableName + "," + expression.toString() + ")";
    }
}
package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

public record LockStatement(String variable) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Check if variable exists in SymbolTable
        if (!state.symbolTable().isDefined(variable)) {
            throw new RuntimeException("LockStatement: variable " + variable + " is not defined");
        }

        // Get the variable value and check it's an IntValue
        Value varValue = state.symbolTable().getVariableValue(variable);
        if (!(varValue instanceof IntValue intValue)) {
            throw new RuntimeException("LockStatement: variable " + variable + " must have type int");
        }

        // Get the lock location
        int foundIndex = intValue.value();

        // Synchronized check and update of the lock
        synchronized (state.lockTable()) {
            // Check if the location exists in LockTable
            if (!state.lockTable().isDefined(foundIndex)) {
                throw new RuntimeException("LockStatement: lock location " + foundIndex + " is not defined in LockTable");
            }

            // Get current lock value
            int lockValue = state.lockTable().get(foundIndex);

            if (lockValue == -1) {
                // Lock is free, acquire it
                state.lockTable().update(foundIndex, state.getId());
            } else {
                // Lock is held by another thread, push the lock statement back
                state.executionStack().push(this);
            }
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Check if variable exists and has type int
        if (!typeEnv.isDefined(variable)) {
            throw new MyException("LockStatement: variable " + variable + " is not declared");
        }

        Type varType = typeEnv.lookup(variable);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("LockStatement: variable " + variable + " must have type int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "lock(" + variable + ")";
    }
}
package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;

public record NewLockStatement(String variable) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Check if variable exists in SymbolTable and has type int
        if (!state.symbolTable().isDefined(variable)) {
            throw new RuntimeException("NewLockStatement: variable " + variable + " is not defined");
        }

        Type varType = state.symbolTable().getVariableType(variable);
        if (!varType.equals(new IntegerType())) {
            throw new RuntimeException("NewLockStatement: variable " + variable + " must have type int");
        }

        // Allocate a new lock in the LockTable (synchronized operation)
        int newLockLocation = state.lockTable().allocateLock();

        // Update the variable with the new lock location
        state.symbolTable().setValue(variable, new IntValue(newLockLocation));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Check if variable exists and has type int
        if (!typeEnv.isDefined(variable)) {
            throw new MyException("NewLockStatement: variable " + variable + " is not declared");
        }

        Type varType = typeEnv.lookup(variable);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("NewLockStatement: variable " + variable + " must have type int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLock(" + variable + ")";
    }
}
package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

/**
 * AwaitStatement: await(var)
 * Where var represents a variable from SymTable which is mapped to a number into the LatchTable
 */
public record AwaitStatement(String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Check if var is in SymTable
        if (!state.symbolTable().isDefined(variableName)) {
            throw new RuntimeException("Await: variable " + variableName + " is not defined");
        }

        // Check if var has type int
        Type varType = state.symbolTable().getVariableType(variableName);
        if (!varType.equals(new IntegerType())) {
            throw new RuntimeException("Await: variable " + variableName + " is not of type int");
        }

        // foundIndex = lookup(SymTable, var)
        Value value = state.symbolTable().getVariableValue(variableName);
        IntValue intValue = (IntValue) value;
        int foundIndex = intValue.value();

        // Check if foundIndex is an index in the LatchTable
        if (!state.latchTable().isDefined(foundIndex)) {
            throw new RuntimeException("Await: index " + foundIndex + " is not in the LatchTable");
        }

        // Get the count value from the LatchTable
        int latchCount = state.latchTable().get(foundIndex);

        // If LatchTable[foundIndex] == 0 then do nothing
        // else push back the await statement (current PrgState must wait)
        if (latchCount != 0) {
            // Push back the await statement
            state.executionStack().push(this);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Verify if var has the type int
        Type varType = typeEnv.lookup(variableName);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("Await: variable " + variableName + " is not of type int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + variableName + ")";
    }
}
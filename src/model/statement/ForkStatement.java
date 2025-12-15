package model.statement;

import exceptions.MyException;
import model.state.*;
import model.type.Type;
import model.utils.MyIDictionary;

public class ForkStatement implements Statement {
    private final Statement statement;

    public ForkStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Create a new execution stack for the forked thread
        ExecutionStack newStack = new LinkedListExecutionStack();
        newStack.push(statement);

        // Clone the symbol table for the new thread
        SymbolTable newSymTable = state.symbolTable().deepCopy();


        // - Cloned symbol table (not shared)
        ProgramState newPrgState = new ProgramState(
            newStack,
            newSymTable,
            state.out(),        // Shared
            state.fileTable(),  // Shared
            state.heap()        // Shared
        );

        // Return the new created PrgState
        return newPrgState;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        statement.typecheck(typeEnv.clone());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}
package model.statement;

import model.state.*;

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

        // Create the new ProgramState (thread) with:
        // - New execution stack containing the fork statement
        // - Cloned symbol table (not shared)
        // - Shared heap, file table, and output (references to parent's)
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
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}
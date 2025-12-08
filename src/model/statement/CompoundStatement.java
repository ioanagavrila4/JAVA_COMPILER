package model.statement;

import model.state.ExecutionStack;
import model.state.ProgramState;

public record CompoundStatement(Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        executionStack.push(this.second);
        executionStack.push(this.first);
        return null;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }
}

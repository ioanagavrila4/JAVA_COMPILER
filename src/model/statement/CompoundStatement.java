package model.statement;

import exceptions.MyException;
import model.state.ExecutionStack;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

public record CompoundStatement(Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        executionStack.push(this.second);
        executionStack.push(this.first);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }
}

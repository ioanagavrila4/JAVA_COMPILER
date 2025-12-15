package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

public class NoOperationStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "nop";
    }
}

package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

public interface Statement {
    ProgramState execute(ProgramState state);
    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}

package model.statement;

import model.state.ProgramState;

public interface Statement {
    ProgramState execute(ProgramState state);
}

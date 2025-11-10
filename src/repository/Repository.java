package repository;

import exceptions.MyException;
import model.state.ProgramState;

public interface Repository {
    void addProgramState(ProgramState program);
    ProgramState getCurrentState();
    void logPrgStateExec() throws MyException;
}

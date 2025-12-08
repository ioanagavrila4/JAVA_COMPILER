package repository;

import exceptions.MyException;
import model.state.ProgramState;

import java.util.List;

public interface Repository {
    void addProgramState(ProgramState program);
    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> prgList);
    void logPrgStateExec(ProgramState prgState) throws MyException;
}

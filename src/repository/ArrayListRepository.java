package repository;

import exceptions.MyException;
import model.state.ProgramState;
import model.statement.Statement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArrayListRepository implements Repository{
    private final List<ProgramState> programStates = new ArrayList<>();
    private final String logFilePath;

    public ArrayListRepository(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void addProgramState(ProgramState program) {
        programStates.add(program);
    }

    @Override
    public ProgramState getCurrentState() {
        return programStates.getFirst();
    }

    @Override
    public void logPrgStateExec() throws MyException {
        ProgramState state = getCurrentState();
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println("ExeStack:");
            List<Statement> stackContents = new ArrayList<>();
            var stack = state.executionStack();
            while (!stack.isEmpty()) {
                stackContents.add(stack.pop());
            }
            for (int i = stackContents.size() - 1; i >= 0; i--) {
                logFile.println(stackContents.get(i).toString());
                stack.push(stackContents.get(i));
            }

            logFile.println("SymTable:");
            logFile.print(state.symbolTable().toString());

            logFile.println("Out:");
            logFile.print(state.out().toString());

            logFile.println("FileTable:");
            logFile.print(state.fileTable().toString());

            logFile.println("Heap:");
            logFile.print(state.heap().toString());

            logFile.println();
        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }
}

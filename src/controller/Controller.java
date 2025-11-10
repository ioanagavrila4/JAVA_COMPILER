package controller;

import exceptions.MyException;
import model.state.*;
import model.statement.Statement;
import repository.Repository;

//record = ii o clasa mai speciala care face immutable data - are automat metode gen equals, hashCode etc

public record Controller(Repository repository) {
    public void addNewProgram(Statement program) {
        var executionStack = new LinkedListExecutionStack();
        executionStack.push(program);

        repository.addProgramState(new ProgramState(
                executionStack,
                new MapSymbolTable(),
                new ArrayListOut(),
                new MapFileTable()
        ));
    }

    public void displayCurrentState() {
        model.state.IO.println(repository.getCurrentState());
    }

    public void executeAllSteps() throws MyException {
        var state = repository.getCurrentState();
        repository.logPrgStateExec();
        while (!state.executionStack().isEmpty()) {
            state = executeOneStep(state);
            repository.logPrgStateExec();
            model.state.IO.println(state);
        }
    }

    private ProgramState executeOneStep(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        if (executionStack.isEmpty()) {
            throw new RuntimeException("Execution stack is empty");
        }

        Statement nextStatement = executionStack.pop();
        return nextStatement.execute(state);
    }
}

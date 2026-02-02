package model.statement;

import exceptions.MyException;
import javafx.util.Pair;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

import java.util.List;
import java.util.ArrayList;

/**
 * Statement for acquiring a counting semaphore
 * Syntax: acquire(var)
 * Attempts to acquire the semaphore identified by var
 * If the semaphore count allows, adds the program to the waiting list
 * Otherwise, pushes itself back to the execution stack
 */
public class AcquireStatement implements Statement {
    private final String var;

    public AcquireStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Get the semaphore index from the variable
        if (!state.symbolTable().isDefined(var)) {
            throw new RuntimeException("Variable " + var + " is not defined");
        }

        Value varValue = state.symbolTable().getVariableValue(var);
        if (!(varValue instanceof IntValue)) {
            throw new RuntimeException("Variable " + var + " is not of integer type");
        }

        int foundIndex = ((IntValue) varValue).value();

        // Check if the semaphore exists
        if (!state.semaphoreTable().containsSemaphore(foundIndex)) {
            throw new RuntimeException("Semaphore with index " + foundIndex + " does not exist");
        }

        // Get the semaphore data
        Pair<Integer, List<Integer>> semaphoreData = state.semaphoreTable().getSemaphore(foundIndex);
        int semaphoreCount = semaphoreData.getKey();
        List<Integer> waitingList = new ArrayList<>(semaphoreData.getValue());

        int currentProgramId = state.getId();
        int waitingListLength = waitingList.size();

        // Check if we can acquire the semaphore
        if (semaphoreCount > waitingListLength) {
            // Check if current program is already in the waiting list
            if (!waitingList.contains(currentProgramId)) {
                // Add current program to the waiting list
                waitingList.add(currentProgramId);
                state.semaphoreTable().updateSemaphore(foundIndex, semaphoreCount, waitingList);
            }
            // else: program is already in the list, do nothing
        } else {
            // Cannot acquire yet, push this statement back on the stack
            state.executionStack().push(this);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Check that var has integer type
        Type varType = typeEnv.lookup(var);
        if (varType == null) {
            throw new MyException("Variable " + var + " is not declared");
        }
        if (!(varType instanceof IntegerType)) {
            throw new MyException("Variable " + var + " must have integer type for semaphore acquire");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "acquire(" + var + ")";
    }
}
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
 * Statement for releasing a counting semaphore
 * Syntax: release(var)
 * Removes the current program from the semaphore's waiting list
 */
public class ReleaseStatement implements Statement {
    private final String var;

    public ReleaseStatement(String var) {
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

        // Remove the current program from the waiting list if it's there
        if (waitingList.contains(currentProgramId)) {
            waitingList.remove(Integer.valueOf(currentProgramId));
            state.semaphoreTable().updateSemaphore(foundIndex, semaphoreCount, waitingList);
        }
        // else: program is not in the list, do nothing

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
            throw new MyException("Variable " + var + " must have integer type for semaphore release");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "release(" + var + ")";
    }
}
package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

import java.util.List;
import java.util.Map;

public class AwaitStatement implements Statement {
    private final String var;

    public AwaitStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // foundIndex = lookup(SymTable, var)
        if (!state.symbolTable().isDefined(var)) {
            throw new RuntimeException("AwaitStatement: variable " + var + " is not defined in SymbolTable");
        }

        Type varType = state.symbolTable().getVariableType(var);
        if (!varType.equals(new IntegerType())) {
            throw new RuntimeException("AwaitStatement: variable " + var + " is not of type int");
        }

        Value value = state.symbolTable().getVariableValue(var);
        if (!(value instanceof IntValue intValue)) {
            throw new RuntimeException("AwaitStatement: variable " + var + " does not have an integer value");
        }

        int foundIndex = intValue.value();

        // Check if foundIndex is in the BarrierTable
        if (!state.barrierTable().isDefined(foundIndex)) {
            throw new RuntimeException("AwaitStatement: index " + foundIndex + " is not in the BarrierTable");
        }

        // Retrieve the entry for that foundIndex
        Map.Entry<Integer, List<Integer>> barrierEntry = state.barrierTable().get(foundIndex);
        int n1 = barrierEntry.getKey();
        List<Integer> list1 = barrierEntry.getValue();

        // Compute the length of the list
        int nl = list1.size();

        // If N1 > NL
        if (n1 > nl) {
            // Check if the current PrgState id is in the list
            if (!list1.contains(state.getId())) {
                // Add the id of the current PrgState to the list
                list1.add(state.getId());
                // Update the barrier table
                state.barrierTable().update(foundIndex, n1, list1);
            }
            // Push back await(var) on the ExeStack
            state.executionStack().push(new AwaitStatement(var));
        }
        // else do nothing (barrier condition is met)

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Verify if var has the type int
        if (!typeEnv.isDefined(var)) {
            throw new MyException("AwaitStatement: variable " + var + " is not defined");
        }

        Type varType = typeEnv.lookup(var);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("AwaitStatement: variable " + var + " is not of type int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}
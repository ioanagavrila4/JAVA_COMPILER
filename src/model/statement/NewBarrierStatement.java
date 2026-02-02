package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

import java.util.ArrayList;

public class NewBarrierStatement implements Statement {
    private final String var;
    private final Expression exp;

    public NewBarrierStatement(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Evaluate the expression
        Value value = exp.evaluate(state.symbolTable(), state.heap());

        // Check if the result is an integer
        if (!(value instanceof IntValue intValue)) {
            throw new RuntimeException("NewBarrierStatement: expression does not evaluate to an integer");
        }

        int nr = intValue.value();

        // Add new barrier to the BarrierTable
        int newFreeLocation = state.barrierTable().add(nr, new ArrayList<>());

        // Check if var exists in SymbolTable and has type int
        if (!state.symbolTable().isDefined(var)) {
            throw new RuntimeException("NewBarrierStatement: variable " + var + " is not defined");
        }

        Type varType = state.symbolTable().getVariableType(var);
        if (!varType.equals(new IntegerType())) {
            throw new RuntimeException("NewBarrierStatement: variable " + var + " is not of type int");
        }

        // Update the variable with the new barrier location
        state.symbolTable().setValue(var, new IntValue(newFreeLocation));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Check if var exists and has type int
        if (!typeEnv.isDefined(var)) {
            throw new MyException("NewBarrierStatement: variable " + var + " is not defined");
        }

        Type varType = typeEnv.lookup(var);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("NewBarrierStatement: variable " + var + " is not of type int");
        }

        // Check if expression has type int
        Type expType = exp.typecheck(typeEnv);
        if (!expType.equals(new IntegerType())) {
            throw new MyException("NewBarrierStatement: expression does not have type int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "newBarrier(" + var + ", " + exp.toString() + ")";
    }
}
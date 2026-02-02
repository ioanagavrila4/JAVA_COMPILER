package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.Value;

/**
 * Statement for creating a new counting semaphore
 * Syntax: createSemaphore(var, exp1)
 * Creates a new semaphore with initial count from exp1 and stores its ID in var
 */
public class CreateSemaphoreStatement implements Statement {
    private final String var;
    private final Expression expression;

    public CreateSemaphoreStatement(String var, Expression expression) {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Evaluate the expression to get the initial count
        Value value = expression.evaluate(state.symbolTable(), state.heap());

        // Check if the result is an integer
        if (!(value instanceof IntValue)) {
            throw new RuntimeException("Expression " + expression + " does not evaluate to an integer");
        }

        int initialCount = ((IntValue) value).value();

        // Check if variable exists and has integer type
        if (!state.symbolTable().isDefined(var)) {
            throw new RuntimeException("Variable " + var + " is not defined");
        }

        Type varType = state.symbolTable().getVariableType(var);
        if (!(varType instanceof IntegerType)) {
            throw new RuntimeException("Variable " + var + " is not of integer type");
        }

        // Create the new semaphore and get its location
        int semaphoreLocation = state.semaphoreTable().createSemaphore(initialCount);

        // Update the variable with the semaphore location
        state.symbolTable().setValue(var, new IntValue(semaphoreLocation));

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
            throw new MyException("Variable " + var + " must have integer type for semaphore creation");
        }

        // Check that expression has integer type
        Type expType = expression.typecheck(typeEnv);
        if (!(expType instanceof IntegerType)) {
            throw new MyException("Semaphore initial count expression must have integer type");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "createSemaphore(" + var + ", " + expression + ")";
    }
}
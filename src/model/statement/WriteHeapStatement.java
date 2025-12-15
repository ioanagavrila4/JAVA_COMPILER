package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.RefValue;
import model.value.Value;

public record WriteHeapStatement(String varName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Check if variable is defined in SymTable
        if (!state.symbolTable().isDefined(varName)) {
            throw new RuntimeException("WriteHeapStatement: variable " + varName + " is not defined");
        }

        // Get variable value and check if it's a RefValue
        Value varValue = state.symbolTable().getVariableValue(varName);
        if (!(varValue instanceof RefValue refValue)) {
            throw new RuntimeException("WriteHeapStatement: variable " + varName + " is not a RefType");
        }

        // Get address from RefValue
        int address = refValue.getAddr();

        // Check if address exists in heap
        if (!state.heap().isDefined(address)) {
            throw new RuntimeException("WriteHeapStatement: address " + address + " is not defined in heap");
        }

        // Evaluate expression
        Value expressionValue = expression.evaluate(state.symbolTable(), state.heap());

        // Get the locationType from the RefValue
        RefType refType = (RefType) refValue.getType();

        // Check if expression type matches the locationType
        if (!expressionValue.getType().equals(refType.getInner())) {
            throw new RuntimeException("WriteHeapStatement: expression type " + expressionValue.getType() +
                " does not match location type " + refType.getInner());
        }

        // Update heap at address
        state.heap().update(address, expressionValue);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar instanceof RefType refType) {
            if (refType.getInner().equals(typexp)) {
                return typeEnv;
            } else {
                throw new MyException("WriteHeap stmt: right hand side and left hand side have different types");
            }
        } else {
            throw new MyException("WriteHeap stmt: variable is not a RefType");
        }
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression.toString() + ")";
    }
}

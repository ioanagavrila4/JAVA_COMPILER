package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.RefValue;
import model.value.Value;

public record NewStatement(String varName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Check if variable exists in SymTable
        if (!state.symbolTable().isDefined(varName)) {
            throw new RuntimeException("NewStatement: variable " + varName + " is not defined");
        }

        // Check if variable type is RefType
        Value varValue = state.symbolTable().getVariableValue(varName);
        if (!(varValue instanceof RefValue refValue)) {
            throw new RuntimeException("NewStatement: variable " + varName + " is not a RefType");
        }

        // Get the locationType from the RefValue
        RefType refType = (RefType) refValue.getType();

        // Evaluate the expression
        Value expressionValue = expression.evaluate(state.symbolTable(), state.heap());

        // Compare types
        if (!expressionValue.getType().equals(refType.getInner())) {
            throw new RuntimeException("NewStatement: expression type " + expressionValue.getType() +
                " does not match variable location type " + refType.getInner());
        }

        // Allocate in heap and get new address
        int newAddress = state.heap().allocate(expressionValue);

        // Update SymTable with new RefValue having the new address
        RefValue newRefValue = new RefValue(newAddress, refType.getInner());
        state.symbolTable().setValue(varName, newRefValue);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp))) {
            return typeEnv;
        } else {
            throw new MyException("NEW stmt: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression.toString() + ")";
    }
}

package model.statement;

import model.expression.Expression;
import model.state.ProgramState;

public record AssignmentStatement(String variableName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        var value = expression.evaluate(state.symbolTable(), state.heap());
        var expressionType = value.getType();
        var variableType = state.symbolTable().getVariableType(variableName);
        if (!expressionType.equals(variableType)) {
            throw new RuntimeException("Different types");
        }
        state.symbolTable().setValue(variableName, value);
        return state;
    }

    @Override
    public String toString() {
        return variableName + "=" + expression.toString();
    }
}

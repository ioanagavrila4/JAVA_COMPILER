package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

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
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(variableName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(typexp)) {
            return typeEnv;
        } else {
            throw new MyException("Assignment: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return variableName + "=" + expression.toString();
    }
}

package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.Value;

public record IfStatement(Expression condition, Statement thenStatement, Statement elseStatement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = condition.evaluate(state.symbolTable(), state.heap());
        if (!value.getType().equals(new BooleanType())) {
            throw new RuntimeException("If condition is not boolean");
        }

        BoolValue booleanValue = (BoolValue) value;
        Statement chosenStatement = booleanValue.value() ?
                thenStatement : elseStatement;
        state.executionStack().push(chosenStatement);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BooleanType())) {
            thenStatement.typecheck(typeEnv.clone());
            elseStatement.typecheck(typeEnv.clone());
            return typeEnv;
        } else {
            throw new MyException("The condition of IF has not the type bool");
        }
    }

    @Override
    public String toString() {
        return "(IF(" + condition.toString() + ") THEN(" + thenStatement.toString() +
                ")ELSE(" + elseStatement.toString() + "))";
    }
}

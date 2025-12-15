package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

public record PrintStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        state.out().add(expression.evaluate(state.symbolTable(), state.heap()));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        expression.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}

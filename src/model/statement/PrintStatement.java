package model.statement;

import model.expression.Expression;
import model.state.ProgramState;

public record PrintStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        state.out().add(expression.evaluate(state.symbolTable(), state.heap()));
        return null;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}

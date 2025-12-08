package model.statement;

import model.state.ProgramState;
import model.type.Type;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        var symbolTable = state.symbolTable();
        symbolTable.declareVariable(type, variableName);
        return null;
    }

    @Override
    public String toString() {
        return type.toString().toLowerCase() + " " + variableName;
    }
}

package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        var symbolTable = state.symbolTable();
        symbolTable.declareVariable(type, variableName);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.add(variableName, type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type.toString().toLowerCase() + " " + variableName;
    }
}

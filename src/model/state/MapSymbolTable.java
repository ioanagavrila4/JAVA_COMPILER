package model.state;

import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MapSymbolTable implements SymbolTable{
    private final Map<String, Value> symbolTable = new HashMap<>();

    @Override
    public void declareVariable(Type type, String variableName) {
        symbolTable.put(variableName, type.getDefaultValue());
    }

    @Override
    public Value getVariableValue(String variableName) {
        return symbolTable.get(variableName);
    }

    @Override
    public Type getVariableType(String variableName) {
        return symbolTable.get(variableName).getType();
    }

    @Override
    public void setValue(String variableName, Value value) {
        symbolTable.put(variableName, value);
    }

    @Override
    public boolean isDefined(String variableName) {
        return symbolTable.containsKey(variableName);
    }

    @Override
    public java.util.Collection<Value> getContent() {
        return symbolTable.values();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Value> entry : symbolTable.entrySet()) {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}

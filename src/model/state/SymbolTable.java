package model.state;

import model.type.Type;
import model.value.Value;

import java.util.Collection;

public interface SymbolTable {
    void declareVariable(Type type, String variableName);

    Value getVariableValue(String variableName);

    Type getVariableType(String variableName);

    void setValue(String variableName, Value value);

    boolean isDefined(String variableName);

    Collection<Value> getContent();
}

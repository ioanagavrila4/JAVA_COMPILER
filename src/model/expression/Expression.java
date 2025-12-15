package model.expression;

import exceptions.MyException;
import model.state.Heap;
import model.state.SymbolTable;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.Value;

public interface Expression {
    Value evaluate(SymbolTable symbolTable, Heap heap);
    Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}

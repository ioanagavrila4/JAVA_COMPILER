package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.StringType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record OpenReadFileStatement(Expression fileNameExpression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = fileNameExpression.evaluate(state.symbolTable(), state.heap());

        if (!value.getType().equals(new StringType())) {
            throw new RuntimeException("OpenReadFile: expression is not a string");
        }

        StringValue fileName = (StringValue) value;

        if (state.fileTable().isDefined(fileName)) {
            throw new RuntimeException("OpenReadFile: file " + fileName.getVal() + " is already opened");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName.getVal()));
            state.fileTable().openFile(fileName, reader);
        } catch (IOException e) {
            throw new RuntimeException("OpenReadFile: error opening file " + fileName.getVal() + ": " + e.getMessage());
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = fileNameExpression.typecheck(typeEnv);
        if (typexp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new MyException("OpenReadFile: expression is not a string");
        }
    }

    @Override
    public String toString() {
        return "openRFile(" + fileNameExpression.toString() + ")";
    }
}

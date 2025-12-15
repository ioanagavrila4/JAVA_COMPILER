package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.StringType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStatement(Expression fileNameExpression, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        if (!state.symbolTable().isDefined(variableName)) {
            throw new RuntimeException("ReadFile: variable " + variableName + " is not defined");
        }

        if (!state.symbolTable().getVariableType(variableName).equals(new IntegerType())) {
            throw new RuntimeException("ReadFile: variable " + variableName + " is not of type int");
        }

        Value value = fileNameExpression.evaluate(state.symbolTable(), state.heap());

        if (!value.getType().equals(new StringType())) {
            throw new RuntimeException("ReadFile: expression is not a string");
        }

        StringValue fileName = (StringValue) value;

        if (!state.fileTable().isDefined(fileName)) {
            throw new RuntimeException("ReadFile: file " + fileName.getVal() + " is not opened");
        }

        BufferedReader reader = state.fileTable().getFileDescriptor(fileName);

        try {
            String line = reader.readLine();
            int intValue;
            if (line == null) {
                intValue = 0;
            } else {
                intValue = Integer.parseInt(line);
            }
            state.symbolTable().setValue(variableName, new IntValue(intValue));
        } catch (IOException e) {
            throw new RuntimeException("ReadFile: error reading from file " + fileName.getVal() + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new RuntimeException("ReadFile: invalid number format in file " + fileName.getVal());
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = fileNameExpression.typecheck(typeEnv);
        if (!typexp.equals(new StringType())) {
            throw new MyException("ReadFile: expression is not a string");
        }
        Type typevar = typeEnv.lookup(variableName);
        if (!typevar.equals(new IntegerType())) {
            throw new MyException("ReadFile: variable is not of type int");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "readFile(" + fileNameExpression.toString() + "," + variableName + ")";
    }
}

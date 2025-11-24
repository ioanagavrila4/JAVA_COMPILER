package model.statement;

import model.expression.Expression;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.Value;

public record CloseReadFileStatement(Expression fileNameExpression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = fileNameExpression.evaluate(state.symbolTable(), state.heap());

        if (!value.getType().equals(new StringType())) {
            throw new RuntimeException("CloseReadFile: expression is not a string");
        }

        StringValue fileName = (StringValue) value;

        if (!state.fileTable().isDefined(fileName)) {
            throw new RuntimeException("CloseReadFile: file " + fileName.getVal() + " is not opened");
        }

        state.fileTable().closeFile(fileName);

        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + fileNameExpression.toString() + ")";
    }
}

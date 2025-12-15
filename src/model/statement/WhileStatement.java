package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.Type;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.Value;

public record WhileStatement(Expression condition, Statement body) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Evaluate the condition
        Value conditionValue = condition.evaluate(state.symbolTable(), state.heap());

        // Check if condition is a boolean
        if (!(conditionValue instanceof BoolValue boolValue)) {
            throw new RuntimeException("WhileStatement: condition expression is not a boolean");
        }

        // If condition is true, push the while statement again and the body to the stack
        if (boolValue.value()) {
            state.executionStack().push(this); // Push while statement back
            state.executionStack().push(body); // Push body to execute first
        }
        // If condition is false, do nothing (while loop ends)

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BooleanType())) {
            body.typecheck(typeEnv.clone());
            return typeEnv;
        } else {
            throw new MyException("The condition of WHILE has not the type bool");
        }
    }

    @Override
    public String toString() {
        return "while (" + condition.toString() + ") " + body.toString();
    }
}

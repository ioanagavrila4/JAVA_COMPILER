package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.Type;
import model.utils.MyIDictionary;

/**
 * Conditional Assignment Statement: v = exp1 ? exp2 : exp3
 * When exp1 is true, the value of exp2 is assigned to v. Otherwise v takes the value of exp3.
 * This statement is transformed into: if (exp1) then v=exp2 else v=exp3
 */
public record ConditionalAssignmentStatement(
        String variableName,
        Expression condition,
        Expression trueExpression,
        Expression falseExpression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Pop the statement (done automatically by the execution stack)
        // Create the following statement: if (exp1) then v=exp2 else v=exp3
        Statement thenBranch = new AssignmentStatement(variableName, trueExpression);
        Statement elseBranch = new AssignmentStatement(variableName, falseExpression);
        Statement ifStatement = new IfStatement(condition, thenBranch, elseBranch);

        // Push the new statement on the stack
        state.executionStack().push(ifStatement);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Verify if exp1 has the type bool
        Type conditionType = condition.typecheck(typeEnv);
        if (!conditionType.equals(new BooleanType())) {
            throw new MyException("Conditional assignment: condition must have type bool");
        }

        // Verify that v, exp2, and exp3 have the same type
        Type variableType = typeEnv.lookup(variableName);
        Type trueExprType = trueExpression.typecheck(typeEnv);
        Type falseExprType = falseExpression.typecheck(typeEnv);

        if (!variableType.equals(trueExprType)) {
            throw new MyException("Conditional assignment: variable type and true expression type differ");
        }

        if (!variableType.equals(falseExprType)) {
            throw new MyException("Conditional assignment: variable type and false expression type differ");
        }

        if (!trueExprType.equals(falseExprType)) {
            throw new MyException("Conditional assignment: true and false expressions have different types");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return variableName + "=(" + condition.toString() + ")?" +
               trueExpression.toString() + ":" + falseExpression.toString();
    }
}
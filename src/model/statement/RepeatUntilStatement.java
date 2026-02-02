package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.expression.NotExpression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.Type;
import model.utils.MyIDictionary;

public class RepeatUntilStatement implements Statement {
    private final Statement stmt1;
    private final Expression exp2;

    public RepeatUntilStatement(Statement stmt1, Expression exp2) {
        this.stmt1 = stmt1;
        this.exp2 = exp2;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Pop the statement (implicit when this method is called)
        // Create the following statement: stmt1;(while(!exp2) stmt1)
        Expression negatedExp = new NotExpression(exp2);
        Statement whileStmt = new WhileStatement(negatedExp, stmt1);
        Statement compoundStmt = new CompoundStatement(stmt1, whileStmt);

        // Push the new statement on the stack
        state.executionStack().push(compoundStmt);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Verify if exp2 has the type bool
        Type typexp = exp2.typecheck(typeEnv);
        if (!typexp.equals(new BooleanType())) {
            throw new MyException("RepeatUntilStatement: condition expression is not of boolean type");
        }

        // Also typecheck the statement stmt1
        stmt1.typecheck(typeEnv.clone());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat " + stmt1.toString() + " until " + exp2.toString();
    }
}
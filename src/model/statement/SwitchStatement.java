package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.expression.RelationalExpression;
import model.state.ProgramState;
import model.type.Type;
import model.utils.MyIDictionary;

/**
 * Switch statement implementation for Toy Language
 * Syntax: switch(exp) (case exp1: stmt1) (case exp2: stmt2) (default: stmt3)
 *
 * Execution transforms the switch into nested if-then-else statements:
 * if(exp==exp1) then stmt1 else (if (exp==exp2) then stmt2 else stmt3)
 */
public class SwitchStatement implements Statement {
    private final Expression expression;
    private final Expression case1Expression;
    private final Statement case1Statement;
    private final Expression case2Expression;
    private final Statement case2Statement;
    private final Statement defaultStatement;

    public SwitchStatement(Expression expression,
                          Expression case1Expression, Statement case1Statement,
                          Expression case2Expression, Statement case2Statement,
                          Statement defaultStatement) {
        this.expression = expression;
        this.case1Expression = case1Expression;
        this.case1Statement = case1Statement;
        this.case2Expression = case2Expression;
        this.case2Statement = case2Statement;
        this.defaultStatement = defaultStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Create the equivalent if-then-else statement
        // if(exp==exp1) then stmt1 else (if (exp==exp2) then stmt2 else stmt3)

        // Create exp==exp1 comparison
        RelationalExpression condition1 = new RelationalExpression(
            expression, case1Expression, "=="
        );

        // Create exp==exp2 comparison
        RelationalExpression condition2 = new RelationalExpression(
            expression, case2Expression, "=="
        );

        // Create inner if statement: if (exp==exp2) then stmt2 else stmt3
        IfStatement innerIf = new IfStatement(condition2, case2Statement, defaultStatement);

        // Create outer if statement: if(exp==exp1) then stmt1 else innerIf
        IfStatement outerIf = new IfStatement(condition1, case1Statement, innerIf);

        // Push the transformed statement onto the execution stack
        state.executionStack().push(outerIf);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Get the types of all expressions
        Type expType = expression.typecheck(typeEnv);
        Type case1Type = case1Expression.typecheck(typeEnv);
        Type case2Type = case2Expression.typecheck(typeEnv);

        // Verify that expression and both case expressions have the same type
        if (!expType.equals(case1Type)) {
            throw new MyException("Switch expression type " + expType +
                                " does not match case1 expression type " + case1Type);
        }

        if (!expType.equals(case2Type)) {
            throw new MyException("Switch expression type " + expType +
                                " does not match case2 expression type " + case2Type);
        }

        // Typecheck all statements
        case1Statement.typecheck(typeEnv.clone());
        case2Statement.typecheck(typeEnv.clone());
        defaultStatement.typecheck(typeEnv.clone());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "switch(" + expression + ") " +
               "(case " + case1Expression + ": " + case1Statement + ") " +
               "(case " + case2Expression + ": " + case2Statement + ") " +
               "(default: " + defaultStatement + ")";
    }
}
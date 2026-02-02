package model.statement;

import exceptions.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.Type;
import model.utils.MyIDictionary;

public record ForStatement(String variable, Expression init, Expression condition, Expression increment, Statement body) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // Create the desugared statement:
        // int v; v=exp1; while(v<exp2) {stmt; v=exp3}

        // int v;
        Statement varDecl = new VariableDeclarationStatement(new IntegerType(), variable);

        // v = exp1;
        Statement initialization = new AssignmentStatement(variable, init);

        // v = exp3
        Statement incrementStmt = new AssignmentStatement(variable, increment);

        // stmt; v=exp3
        Statement bodyWithIncrement = new CompoundStatement(body, incrementStmt);

        // while(v<exp2) {stmt; v=exp3}
        Statement whileLoop = new WhileStatement(condition, bodyWithIncrement);

        // int v; v=exp1; while(v<exp2) {stmt; v=exp3}
        Statement desugared = new CompoundStatement(
            varDecl,
            new CompoundStatement(initialization, whileLoop)
        );

        // Push the desugared statement onto the execution stack
        state.executionStack().push(desugared);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // First, add the variable to the type environment as int
        // This is needed because the condition might reference the variable
        MyIDictionary<String, Type> newEnv = typeEnv.clone();
        newEnv.add(variable, new IntegerType());

        // Check that init expression has type int
        Type initType = init.typecheck(typeEnv);  // Use original env for init
        if (!initType.equals(new IntegerType())) {
            throw new MyException("FOR statement: init expression must have type int");
        }

        // Check that condition expression has type boolean (use new env since it might reference v)
        Type conditionType = condition.typecheck(newEnv);
        if (!conditionType.equals(new BooleanType())) {
            throw new MyException("FOR statement: condition expression must have type boolean");
        }

        // Check that increment expression has type int (use new env since it might reference v)
        Type incrementType = increment.typecheck(newEnv);
        if (!incrementType.equals(new IntegerType())) {
            throw new MyException("FOR statement: increment expression must have type int");
        }

        // Typecheck the body in the new environment
        body.typecheck(newEnv);

        return typeEnv;
    }

    @Override
    public String toString() {
        return "for(" + variable + "=" + init + "; " + variable + "<" + condition + "; " + variable + "=" + increment + ") " + body;
    }
}
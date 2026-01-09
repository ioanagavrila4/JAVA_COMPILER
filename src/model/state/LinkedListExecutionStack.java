package model.state;

import model.statement.Statement;

import java.util.LinkedList;
import java.util.List;

public class LinkedListExecutionStack implements ExecutionStack {
    private final List<Statement> statements =  new LinkedList<>();

    @Override
    public void push(Statement statement) {
        statements.addFirst(statement);
    }

    @Override
    public Statement pop() {
        if ( statements.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return statements.removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return statements.isEmpty();
    }

    @Override
    public List<Statement> toList() {
        return new LinkedList<>(statements);
    } //pt gui

    @Override
    public String toString() {
        return "ExecutionStack" + statements;
    }
}

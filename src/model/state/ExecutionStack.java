package model.state;

import model.statement.Statement;
import java.util.List;

public interface ExecutionStack {
    void push(Statement statement);

    Statement pop();

    boolean isEmpty();

    List<Statement> toList();
}

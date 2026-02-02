package model.state;

import exceptions.MyException;
import model.statement.Statement;

public class ProgramState {
    private static int nextId = 0;
    private final int id;
    private final ExecutionStack executionStack;
    private final SymbolTable symbolTable;
    private final Out out;
    private final FileTable fileTable;
    private final Heap heap;
    private final SemaphoreTable semaphoreTable;

    // Static synchronized method to generate unique IDs
    private static synchronized int generateId() {
        return ++nextId;
    }

    // Constructor for initial program state
    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable,
                       Out out, FileTable fileTable, Heap heap, SemaphoreTable semaphoreTable) {
        this.id = generateId();
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.semaphoreTable = semaphoreTable;
    }

    // Constructor for forked program state with specific id
    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable,
                       Out out, FileTable fileTable, Heap heap, SemaphoreTable semaphoreTable, int id) {
        this.id = id;
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.semaphoreTable = semaphoreTable;
    }

    // Getters
    public int getId() {
        return id;
    }

    public ExecutionStack executionStack() {
        return executionStack;
    }

    public SymbolTable symbolTable() {
        return symbolTable;
    }

    public Out out() {
        return out;
    }

    public FileTable fileTable() {
        return fileTable;
    }

    public Heap heap() {
        return heap;
    }

    public SemaphoreTable semaphoreTable() {
        return semaphoreTable;
    }

    // Check if program is not completed
    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    // Execute one step of the program
    public ProgramState oneStep() throws MyException {
        if (executionStack.isEmpty()) {
            throw new MyException("prgstate stack is empty");
        }
        Statement crtStmt = executionStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "Id=" + id + "\n" +
               "ExeStack:\n" + executionStack.toString() + "\n" +
               "SymTable:\n" + symbolTable.toString() + "\n" +
               "Out:\n" + out.toString() + "\n" +
               "FileTable:\n" + fileTable.toString() + "\n" +
               "Heap:\n" + heap.toString() + "\n" +
               "SemaphoreTable:\n" + semaphoreTable.toString() + "\n";
    }
}
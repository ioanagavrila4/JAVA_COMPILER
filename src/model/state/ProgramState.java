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
    private final BarrierTable barrierTable;

    // Static synchronized method to generate unique IDs
    private static synchronized int generateId() {
        return ++nextId;
    }

    // Constructor for initial program state with BarrierTable
    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable,
                       Out out, FileTable fileTable, Heap heap, BarrierTable barrierTable) {
        this.id = generateId();
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.barrierTable = barrierTable;
    }

    // Constructor for initial program state (backward compatibility)
    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable,
                       Out out, FileTable fileTable, Heap heap) {
        this.id = generateId();
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.barrierTable = new MapBarrierTable();
    }

    // Constructor for forked program state with specific id
    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable,
                       Out out, FileTable fileTable, Heap heap, int id) {
        this.id = id;
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.barrierTable = new MapBarrierTable();
    }

    // Constructor for forked program state with specific id and BarrierTable
    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable,
                       Out out, FileTable fileTable, Heap heap, BarrierTable barrierTable, int id) {
        this.id = id;
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.barrierTable = barrierTable;
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

    public BarrierTable barrierTable() {
        return barrierTable;
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
               "BarrierTable:\n" + barrierTable.toString() + "\n";
    }
}
package controller;

import exceptions.MyException;
import model.state.*;
import model.statement.Statement;
import model.type.Type;
import model.utils.MyDictionary;
import model.utils.MyIDictionary;
import model.value.RefValue;
import model.value.Value;
import repository.Repository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final Repository repository;
    private ExecutorService executor;

    public Controller(Repository repository) {
        this.repository = repository;
    }

    public void addNewProgram(Statement program) throws MyException {
        // Type check the program before creating the ProgramState
        MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
        program.typecheck(typeEnv);

        // If type checking passes, create the ProgramState
        var executionStack = new LinkedListExecutionStack();
        executionStack.push(program);

        repository.addProgramState(new ProgramState(
                executionStack,
                new MapSymbolTable(),
                new ArrayListOut(),
                new MapFileTable(),
                new MapHeap()
        ));
    }

    public void displayCurrentState() {
        List<ProgramState> prgList = repository.getPrgList();
        if (!prgList.isEmpty()) {
            model.state.IO.println(prgList.get(0));
        }
    }

    // Getter for program states
    //pt gui
    public List<ProgramState> getProgramStates() {
        return repository.getPrgList();
    }

    // Setter for program states
    public void setProgramStates(List<ProgramState> prgList) {
        repository.setPrgList(prgList);
    }

    // Remove completed programs from the list
    private List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    // Execute one step for all programs concurrently
    //schimbare gui: sa fie publice:))))
    public void oneStepForAllPrg(List<ProgramState> prgList) throws MyException {
        // pt gui ca sa verificare si initializare automata
        if (executor == null) {
            executor = Executors.newFixedThreadPool(2);
        }

        // Before execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        // Prepare the list of callables
        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (() -> {
                    try {
                        return p.oneStep();
                    } catch (MyException e) {
                        System.err.println("Error in thread " + p.getId() + ": " + e.getMessage());
                        return null;
                    }
                }))
                .collect(Collectors.toList());

        // Start the execution of the callables
        // returnam o lista de prg statementuri
        List<ProgramState> newPrgList;
        try {
            newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            System.err.println("Error getting future result: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new MyException("Executor interrupted: " + e.getMessage());
        }

        // Add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);

        // After execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        // Save the current programs in the repository
        repository.setPrgList(prgList);
    }

    // New allStep method for concurrent execution
    public void allStep() throws MyException {
        executor = Executors.newFixedThreadPool(2);

        // Remove the completed programs
        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());

        while (prgList.size() > 0) {
            // Call conservative garbage collector before each step
            conservativeGarbageCollector(prgList);

            // Execute one step for all programs
            oneStepForAllPrg(prgList);

            // Remove the completed programs
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow();

        // Update the repository state
        repository.setPrgList(prgList);
    }

    public void executeAllSteps() throws MyException {
        allStep();
    }

    // Method to shutdown executor when done
    public void shutdownExecutor() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    public void conservativeGarbageCollector(List<ProgramState> prgList) {
        if (prgList.isEmpty()) return;

        // Get all addresses from all symbol tables
        List<Integer> symTableAddresses = prgList.stream()
                .map(p -> getAddrFromSymTable(p.symbolTable().getContent()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // Get the shared heap (all programs share the same heap)
        Heap heap = prgList.get(0).heap();
        Map<Integer, Value> heapContent = heap.getContent();

        // Apply safe garbage collector
        Map<Integer, Value> newHeap = safeGarbageCollector(symTableAddresses, heapContent);

        // Update the heap
        heap.setContent(newHeap);
    }

    // Extracts all addresses from RefValues in the SymbolTable
    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    // Extracts all addresses from RefValues in the Heap
    private List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        // Start with addresses from SymTable
        Set<Integer> reachableAddresses = new HashSet<>(symTableAddr);

        // Keep adding addresses from heap values until no new addresses are found
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Integer> heapAddresses = getAddrFromHeap(
                heap.entrySet().stream()
                    .filter(e -> reachableAddresses.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList())
            );

            for (Integer addr : heapAddresses) {
                if (!reachableAddresses.contains(addr)) {
                    reachableAddresses.add(addr);
                    changed = true;
                }
            }
        }

        // Return only the reachable heap entries
        return heap.entrySet().stream()
                .filter(e -> reachableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
package controller;

import exceptions.MyException;
import model.state.*;
import model.statement.Statement;
import model.value.RefValue;
import model.value.Value;
import repository.Repository;

import java.util.*;
import java.util.stream.Collectors;

//record = ii o clasa mai speciala care face immutable data - are automat metode gen equals, hashCode etc
//o clasa separat in pentru alocare - inainte alocare propriu zisa - verificare referinta variabila
public record Controller(Repository repository) {
    public void addNewProgram(Statement program) {
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
        model.state.IO.println(repository.getCurrentState());
    }

    public void executeAllSteps() throws MyException {
        var state = repository.getCurrentState();
        repository.logPrgStateExec();
        while (!state.executionStack().isEmpty()) {
            state = executeOneStep(state);
            // Run garbage collector after each step
            state.heap().setContent(
                safeGarbageCollector(
                    getAddrFromSymTable(state.symbolTable().getContent()),
                    state.heap().getContent()
                )
            );
            repository.logPrgStateExec();
            model.state.IO.println(state);
        }
    }

    private ProgramState executeOneStep(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        if (executionStack.isEmpty()) {
            throw new RuntimeException("Execution stack is empty");
        }

        Statement nextStatement = executionStack.pop();
        return nextStatement.execute(state);
    }

    // Garbage Collector helper methods

    /**
     * Extracts all addresses from RefValues in the SymbolTable
     */
    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    /**
     * Extracts all addresses from RefValues in the Heap
     */
    private List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

   //Pornește cu adresele direct accesibile din SymbolTable
//Pentru fiecare adresă accesibilă, verifică dacă valoarea din heap conține alte referințe
//Adaugă noile referințe găsite la setul de adrese accesibile
//Repetă până când nu mai găsește referințe noi
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

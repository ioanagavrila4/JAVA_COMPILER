package gui;

import controller.Controller;
import exceptions.MyException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.state.Heap;
import model.state.ProgramState;
import model.value.Value;

import java.util.*;
import java.util.stream.Collectors;

public class MainWindowController {
    @FXML
    private TextField numberOfPrgStatesTextField;

    @FXML
    private TableView<HeapEntry> heapTableView;

    @FXML
    private TableColumn<HeapEntry, Integer> heapAddressColumn;

    @FXML
    private TableColumn<HeapEntry, String> heapValueColumn;

    @FXML
    private ListView<String> outListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> prgStateIdentifiersListView;

    @FXML
    private TableView<SymTableEntry> symTableView;

    @FXML
    private TableColumn<SymTableEntry, String> symVariableNameColumn;

    @FXML
    private TableColumn<SymTableEntry, String> symValueColumn;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<BarrierEntry> barrierTableView;

    @FXML
    private TableColumn<BarrierEntry, Integer> barrierIndexColumn;

    @FXML
    private TableColumn<BarrierEntry, Integer> barrierValueColumn;

    @FXML
    private TableColumn<BarrierEntry, String> barrierListColumn;

    private Controller controller;

    // Keep references to shared components for display after completion
    private ProgramState lastProgramState;

    @FXML
    public void initialize() {
        // Initialize heap table columns
        heapAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        heapValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Initialize symbol table columns
        symVariableNameColumn.setCellValueFactory(new PropertyValueFactory<>("variableName"));
        symValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Initialize barrier table columns
        barrierIndexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        barrierValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        barrierListColumn.setCellValueFactory(new PropertyValueFactory<>("list"));

        // Set up program state selection listener
        prgStateIdentifiersListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    updateSelectedProgramState(newValue);
                }
            }
        );

        // Set up button action
        runOneStepButton.setOnAction(e -> runOneStep());
    }

    public void setController(Controller controller) {
        this.controller = controller;
        populateInitialData();
    }

    private void populateInitialData() {
        // Store initial program state for shared components
        List<ProgramState> prgList = controller.getProgramStates();
        if (!prgList.isEmpty()) {
            lastProgramState = prgList.get(0);
        }

        updateAllComponents();

        // Select first program state if available
        if (!prgList.isEmpty()) {
            prgStateIdentifiersListView.getSelectionModel().selectFirst();
        }
    }

    private void updateAllComponents() {
        List<ProgramState> prgList = controller.getProgramStates();

        // Update number of program states
        numberOfPrgStatesTextField.setText(String.valueOf(prgList.size()));

        // Update program state identifiers list
        List<Integer> prgIds = prgList.stream()
                .map(ProgramState::getId)
                .collect(Collectors.toList());
        prgStateIdentifiersListView.setItems(FXCollections.observableArrayList(prgIds));

        // Keep reference to a program state for shared components
        if (!prgList.isEmpty()) {
            lastProgramState = prgList.get(0);
        }

        // Update shared components (Out, Heap, FileTable) - use lastProgramState if available
        ProgramState stateForSharedComponents = !prgList.isEmpty() ? prgList.get(0) : lastProgramState;

        if (stateForSharedComponents != null) {
            // Update heap table
            updateHeapTable(stateForSharedComponents.heap());

            // Update output list
            updateOutputList(stateForSharedComponents);

            // Update file table
            updateFileTable(stateForSharedComponents);

            // Update barrier table
            updateBarrierTable(stateForSharedComponents);
        }

        // Update selected program state details
        Integer selectedId = prgStateIdentifiersListView.getSelectionModel().getSelectedItem();
        if (selectedId != null) {
            updateSelectedProgramState(selectedId);
        }
    }

    private void updateHeapTable(Heap heap) {
        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        Map<Integer, Value> heapContent = heap.getContent();

        for (Map.Entry<Integer, Value> entry : heapContent.entrySet()) {
            heapEntries.add(new HeapEntry(entry.getKey(), entry.getValue().toString()));
        }

        heapTableView.setItems(heapEntries);
    }

    private void updateOutputList(ProgramState prgState) {
        List<String> output = new ArrayList<>();
        for (Value val : prgState.out().getContent()) {
            output.add(val.toString());
        }
        outListView.setItems(FXCollections.observableArrayList(output));
    }

    private void updateFileTable(ProgramState prgState) {
        List<String> fileNames = new ArrayList<>();
        for (model.value.StringValue fileName : prgState.fileTable().getContent().keySet()) {
            fileNames.add(fileName.getVal());
        }
        fileTableListView.setItems(FXCollections.observableArrayList(fileNames));
    }

    private void updateBarrierTable(ProgramState prgState) {
        ObservableList<BarrierEntry> barrierEntries = FXCollections.observableArrayList();
        Map<Integer, Map.Entry<Integer, List<Integer>>> barrierContent = prgState.barrierTable().getContent();

        for (Map.Entry<Integer, Map.Entry<Integer, List<Integer>>> entry : barrierContent.entrySet()) {
            int index = entry.getKey();
            int value = entry.getValue().getKey();
            String list = entry.getValue().getValue().toString();
            barrierEntries.add(new BarrierEntry(index, value, list));
        }

        barrierTableView.setItems(barrierEntries);
    }

    private void updateSelectedProgramState(Integer prgId) {
        List<ProgramState> prgList = controller.getProgramStates();
        ProgramState selectedPrg = prgList.stream()
                .filter(p -> p.getId() == prgId)
                .findFirst()
                .orElse(null);

        if (selectedPrg != null) {
            // Update symbol table
            updateSymbolTable(selectedPrg);

            // Update execution stack
            updateExecutionStack(selectedPrg);
        }
    }

    private void updateSymbolTable(ProgramState prgState) {
        ObservableList<SymTableEntry> symTableEntries = FXCollections.observableArrayList();
        Map<String, Value> symTableContent = prgState.symbolTable().getContentMap();

        for (Map.Entry<String, Value> entry : symTableContent.entrySet()) {
            symTableEntries.add(new SymTableEntry(entry.getKey(), entry.getValue().toString()));
        }

        symTableView.setItems(symTableEntries);
    }

    private void updateExecutionStack(ProgramState prgState) {
        List<String> exeStackList = new ArrayList<>();
        List<model.statement.Statement> stack = prgState.executionStack().toList();

        for (model.statement.Statement stmt : stack) {
            exeStackList.add(stmt.toString());
        }

        exeStackListView.setItems(FXCollections.observableArrayList(exeStackList));
    }

    @FXML
    private void runOneStep() {
        if (controller == null) {
            showAlert("Error", "Controller not initialized");
            return;
        }

        // Get current program states
        List<ProgramState> prgList = controller.getProgramStates();

        // Check if there are any non-completed programs
        prgList = removeCompletedPrg(prgList);

        if (prgList.isEmpty()) {
            showAlert("Info", "All programs completed");
            controller.shutdownExecutor();
            runOneStepButton.setDisable(true);
            return;
        }

        try {
            // Run garbage collector
            controller.conservativeGarbageCollector(prgList);

            // Execute one step for all programs
            controller.oneStepForAllPrg(prgList);

            // Get the updated program states after execution
            prgList = controller.getProgramStates();

            // Remove completed programs
            prgList = removeCompletedPrg(prgList);
            controller.setProgramStates(prgList);

            // Update UI
            updateAllComponents();

            if (prgList.isEmpty()) {
                showAlert("Info", "All programs completed");
                controller.shutdownExecutor();
                runOneStepButton.setDisable(true);
            }

        } catch (MyException e) {
            showAlert("Execution Error", e.getMessage());
        } catch (Exception e) {
            showAlert("Unexpected Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Helper class for heap table
    public static class HeapEntry {
        private final SimpleIntegerProperty address;
        private final SimpleStringProperty value;

        public HeapEntry(int address, String value) {
            this.address = new SimpleIntegerProperty(address);
            this.value = new SimpleStringProperty(value);
        }

        public int getAddress() {
            return address.get();
        }

        public String getValue() {
            return value.get();
        }
    }

    // Helper class for symbol table
    public static class SymTableEntry {
        private final SimpleStringProperty variableName;
        private final SimpleStringProperty value;

        public SymTableEntry(String variableName, String value) {
            this.variableName = new SimpleStringProperty(variableName);
            this.value = new SimpleStringProperty(value);
        }

        public String getVariableName() {
            return variableName.get();
        }

        public String getValue() {
            return value.get();
        }
    }

    // Helper class for barrier table
    public static class BarrierEntry {
        private final SimpleIntegerProperty index;
        private final SimpleIntegerProperty value;
        private final SimpleStringProperty list;

        public BarrierEntry(int index, int value, String list) {
            this.index = new SimpleIntegerProperty(index);
            this.value = new SimpleIntegerProperty(value);
            this.list = new SimpleStringProperty(list);
        }

        public int getIndex() {
            return index.get();
        }

        public int getValue() {
            return value.get();
        }

        public String getList() {
            return list.get();
        }
    }
}
package gui;

import controller.Controller;
import exceptions.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import model.expression.*;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.RefType;
import model.type.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import repository.ArrayListRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramSelectionController {
    @FXML
    private ListView<ProgramWrapper> programListView;

    private List<ProgramWrapper> programs;

    @FXML
    public void initialize() {
        // Create all the example programs
        programs = createPrograms();

        // Set up the ListView
        ObservableList<ProgramWrapper> programList = FXCollections.observableArrayList(programs);
        programListView.setItems(programList);
        programListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Handle double-click to execute program
        programListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                executeSelectedProgram();
            }
        });
    }

    @FXML
    private void executeSelectedProgram() {
        ProgramWrapper selected = programListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a program to execute.");
            return;
        }

        try {
            // Create repository and controller for the selected program
            String logFile = "log_gui_" + System.currentTimeMillis() + ".txt";
            ArrayListRepository repository = new ArrayListRepository(logFile);
            Controller controller = new Controller(repository);
            controller.addNewProgram(selected.getProgram());

            // Open the main execution window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = loader.load();

            // Pass the controller to the main window
            MainWindowController mainController = loader.getController();
            mainController.setController(controller);

            // Create and show the new window
            Stage stage = new Stage();
            stage.setTitle("Program Execution - " + selected.toString());
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();

            // Close the selection window
            Stage currentStage = (Stage) programListView.getScene().getWindow();
            currentStage.close();

        } catch (MyException e) {
            showAlert("Type Check Error", "Type checking failed: " + e.getMessage());
        } catch (IOException e) {
            showAlert("Loading Error", "Failed to load main window: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private List<ProgramWrapper> createPrograms() {
        List<ProgramWrapper> programList = new ArrayList<>();

        // Example 1: int v; v=2; Print(v)
        Statement ex1 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        programList.add(new ProgramWrapper(ex1, "Example 1: Simple assignment and print"));

        // Example 2: int a; a=2+3*5; int b; b=a-4/2+7; Print(b)
        Statement ex2 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntegerType(), "b"),
                        new CompoundStatement(
                                new AssignmentStatement("a",
                                        new ArithmeticExpression(
                                                new ValueExpression(new IntValue(2)),
                                                new ArithmeticExpression(
                                                        new ValueExpression(new IntValue(3)),
                                                        new ValueExpression(new IntValue(5)),
                                                        '*'),
                                                '+')),
                                new CompoundStatement(
                                        new AssignmentStatement("b",
                                                new ArithmeticExpression(
                                                        new ArithmeticExpression(
                                                                new VariableExpression("a"),
                                                                new ArithmeticExpression(
                                                                        new ValueExpression(new IntValue(4)),
                                                                        new ValueExpression(new IntValue(2)),
                                                                        '/'),
                                                                '-'),
                                                        new ValueExpression(new IntValue(7)),
                                                        '+')),
                                        new PrintStatement(new VariableExpression("b"))))));
        programList.add(new ProgramWrapper(ex2, "Example 2: Arithmetic expressions"));

        // Example 3: bool a; a=false; int v; If a Then v=2 Else v=3; Print(v)
        Statement ex3 = new CompoundStatement(
                new VariableDeclarationStatement(new BooleanType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntegerType(), "v"),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ValueExpression(new BoolValue(false))),
                                new CompoundStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                                new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
        programList.add(new ProgramWrapper(ex3, "Example 3: If statement"));

        // Example 4: File operations
        Statement ex4 = new CompoundStatement(
                new VariableDeclarationStatement(new StringType(), "varf"),
                new CompoundStatement(
                        new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompoundStatement(
                                new OpenReadFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(
                                        new VariableDeclarationStatement(new IntegerType(), "varc"),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFileStatement(new VariableExpression("varf"))))))))));
        programList.add(new ProgramWrapper(ex4, "Example 4: File operations"));

        // Example 5: Heap allocation and reading
        Statement ex5 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntegerType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new RefType(new IntegerType())), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression(
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5)),
                                                        '+')))))));
        programList.add(new ProgramWrapper(ex5, "Example 5: Heap allocation and reading"));

        // Example 6: Heap writing
        Statement ex6 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntegerType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression(
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                new ValueExpression(new IntValue(5)),
                                                '+'))))));
        programList.add(new ProgramWrapper(ex6, "Example 6: Heap writing"));

        // Example 7: While statement
        Statement ex7 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(
                                new WhileStatement(
                                        new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                        new VariableExpression("v"),
                                                        new ValueExpression(new IntValue(1)),
                                                        '-')))),
                                new PrintStatement(new VariableExpression("v")))));
        programList.add(new ProgramWrapper(ex7, "Example 7: While loop"));

        // Example 8: Garbage collector test
        Statement ex8 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntegerType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new RefType(new IntegerType())), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));
        programList.add(new ProgramWrapper(ex8, "Example 8: Garbage collector test"));

        // Example 11: Fork statement
        Statement ex11 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new IntegerType()), "a"),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(
                                        new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));
        programList.add(new ProgramWrapper(ex11, "Example 11: Fork - concurrent execution"));

        return programList;
    }

    // Wrapper class for programs to display them nicely in ListView
    private static class ProgramWrapper {
        private final Statement program;
        private final String description;

        public ProgramWrapper(Statement program, String description) {
            this.program = program;
            this.description = description;
        }

        public Statement getProgram() {
            return program;
        }

        @Override
        public String toString() {
            return description + "\n" + program.toString();
        }
    }
}
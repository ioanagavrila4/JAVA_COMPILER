import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.type.IntegerType;
import model.type.RefType;
import model.value.IntValue;
import repository.ArrayListRepository;

public class TestFork {
    public static void main(String[] args) {
        try {
            // Example from PDF: Fork statement - concurrent execution
            // int v; Ref int a; v=10; new(a,22);
            // fork(wH(a,30); v=32; print(v); print(rH(a)));
            // print(v); print(rH(a))
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

            ArrayListRepository repo = new ArrayListRepository("test_fork_output.txt");
            Controller controller = new Controller(repo);
            controller.addNewProgram(ex11);

            System.out.println("Running fork example...\n");
            controller.allStep();

            System.out.println("\nProgram execution completed.");
            System.out.println("Check test_fork_output.txt for detailed execution log.");

            // Print final output
            var prgList = repo.getPrgList();
            if (!prgList.isEmpty()) {
                var firstPrg = prgList.get(0);
                System.out.println("\nFinal output:");
                System.out.println(firstPrg.out());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
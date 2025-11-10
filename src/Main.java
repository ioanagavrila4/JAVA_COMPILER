import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.type.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import repository.ArrayListRepository;
import view.ExitCommand;
import view.RunExampleCommand;
import view.TextMenu;

void main() {
    // Example 1: int v; v=2; Print(v)
    Statement ex1 = new CompoundStatement(
            new VariableDeclarationStatement(new IntegerType(), "v"),
            new CompoundStatement(
                    new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                    new PrintStatement(new VariableExpression("v"))));

    ArrayListRepository repo1 = new ArrayListRepository("log1.txt");
    Controller ctr1 = new Controller(repo1);
    ctr1.addNewProgram(ex1);

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

    ArrayListRepository repo2 = new ArrayListRepository("log2.txt");
    Controller ctr2 = new Controller(repo2);
    ctr2.addNewProgram(ex2);

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

    ArrayListRepository repo3 = new ArrayListRepository("log3.txt");
    Controller ctr3 = new Controller(repo3);
    ctr3.addNewProgram(ex3);

    // Example 4: File operations example
    // string varf; varf="test.in"; openRFile(varf); int varc;
    // readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)
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

    ArrayListRepository repo4 = new ArrayListRepository("log4.txt");
    Controller ctr4 = new Controller(repo4);
    ctr4.addNewProgram(ex4);

    TextMenu menu = new TextMenu();
    menu.addCommand(new ExitCommand("0", "exit"));
    menu.addCommand(new RunExampleCommand("1", ex1.toString(), ctr1));
    menu.addCommand(new RunExampleCommand("2", ex2.toString(), ctr2));
    menu.addCommand(new RunExampleCommand("3", ex3.toString(), ctr3));
    menu.addCommand(new RunExampleCommand("4", ex4.toString(), ctr4));
    menu.show();
}
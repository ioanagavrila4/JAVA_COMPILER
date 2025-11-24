import controller.Controller;
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

    // Example 5: Heap allocation and reading
    // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)
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

    ArrayListRepository repo5 = new ArrayListRepository("log5.txt");
    Controller ctr5 = new Controller(repo5);
    ctr5.addNewProgram(ex5);

    // Example 6: Heap writing
    // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5)
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

    ArrayListRepository repo6 = new ArrayListRepository("log6.txt");
    Controller ctr6 = new Controller(repo6);
    ctr6.addNewProgram(ex6);

    // Example 7: While statement
    // int v; v=4; (while (v>0) print(v); v=v-1); print(v)
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

    ArrayListRepository repo7 = new ArrayListRepository("log7.txt");
    Controller ctr7 = new Controller(repo7);
    ctr7.addNewProgram(ex7);

    // Example 8: Garbage collector test with nested heap references
    // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))
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

    ArrayListRepository repo8 = new ArrayListRepository("log8.txt");
    Controller ctr8 = new Controller(repo8);
    ctr8.addNewProgram(ex8);

    // Example 9: Combined test - heap, while, and garbage collection
    // Ref int v; new(v,0); int i; i=3; while(i>0) (new(v, rH(v)+1); i=i-1); print(rH(v))
    Statement ex9 = new CompoundStatement(
            new VariableDeclarationStatement(new RefType(new IntegerType()), "v"),
            new CompoundStatement(
                    new NewStatement("v", new ValueExpression(new IntValue(0))),
                    new CompoundStatement(
                            new VariableDeclarationStatement(new IntegerType(), "i"),
                            new CompoundStatement(
                                    new AssignmentStatement("i", new ValueExpression(new IntValue(3))),
                                    new CompoundStatement(
                                            new WhileStatement(
                                                    new RelationalExpression(new VariableExpression("i"), new ValueExpression(new IntValue(0)), ">"),
                                                    new CompoundStatement(
                                                            new NewStatement("v", new ArithmeticExpression(
                                                                    new ReadHeapExpression(new VariableExpression("v")),
                                                                    new ValueExpression(new IntValue(1)),
                                                                    '+')),
                                                            new AssignmentStatement("i", new ArithmeticExpression(
                                                                    new VariableExpression("i"),
                                                                    new ValueExpression(new IntValue(1)),
                                                                    '-')))),
                                            new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))))))));

    ArrayListRepository repo9 = new ArrayListRepository("log9.txt");
    Controller ctr9 = new Controller(repo9);
    ctr9.addNewProgram(ex9);

    TextMenu menu = new TextMenu();
    menu.addCommand(new ExitCommand("0", "exit"));
    menu.addCommand(new RunExampleCommand("1", ex1.toString(), ctr1));
    menu.addCommand(new RunExampleCommand("2", ex2.toString(), ctr2));
    menu.addCommand(new RunExampleCommand("3", ex3.toString(), ctr3));
    menu.addCommand(new RunExampleCommand("4", ex4.toString(), ctr4));
    menu.addCommand(new RunExampleCommand("5", ex5.toString(), ctr5));
    menu.addCommand(new RunExampleCommand("6", ex6.toString(), ctr6));
    menu.addCommand(new RunExampleCommand("7", ex7.toString(), ctr7));
    menu.addCommand(new RunExampleCommand("8", ex8.toString(), ctr8));
    menu.addCommand(new RunExampleCommand("9", ex9.toString(), ctr9));
    menu.show();
}
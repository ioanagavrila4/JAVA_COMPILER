import controller.Controller;
import exceptions.MyException;
import model.expression.*;
import model.statement.*;
import model.type.BooleanType;
import model.type.IntegerType;
import model.value.BoolValue;
import model.value.IntValue;
import repository.ArrayListRepository;

public class TestTypeChecker {
    public static void main(String[] args) {
        
        // Test 1: Valid program (should pass)
        System.out.println("Test 1: Valid program (int v; v=2; print(v))");
        Statement valid = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));

        ArrayListRepository repo1 = new ArrayListRepository("test1.txt");
        Controller ctr1 = new Controller(repo1);
        try {
            ctr1.addNewProgram(valid);
            System.out.println("Type checking PASSED - Program is well-typed\n");
        } catch (MyException e) {
            System.out.println("Type checking FAILED: " + e.getMessage() + "\n");
        }

        // Test 2: Type error - assigning boolean to int variable
        System.out.println("Test 2: Type error (int v; v=false)");
        Statement typeError1 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new AssignmentStatement("v", new ValueExpression(new BoolValue(false))));

        ArrayListRepository repo2 = new ArrayListRepository("test2.txt");
        Controller ctr2 = new Controller(repo2);
        try {
            ctr2.addNewProgram(typeError1);
            System.out.println("UNEXPECTED: Type checking passed but should have failed!\n");
        } catch (MyException e) {
            System.out.println("Type checking correctly FAILED: " + e.getMessage() + "\n");
        }

        // Test 3: Type error - using undefined variable
        System.out.println("Test 3: Undefined variable (print(x))");
        Statement typeError2 = new PrintStatement(new VariableExpression("x"));

        ArrayListRepository repo3 = new ArrayListRepository("test3.txt");
        Controller ctr3 = new Controller(repo3);
        try {
            ctr3.addNewProgram(typeError2);
            System.out.println("UNEXPECTED: Type checking passed but should have failed!\n");
        } catch (MyException e) {
            System.out.println("Type checking correctly FAILED: " + e.getMessage() + "\n");
        }

        // Test 4: Type error - if condition not boolean
        System.out.println("Test 4: If condition not boolean (if 5 then v=1 else v=2)");
        Statement typeError3 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new IfStatement(
                        new ValueExpression(new IntValue(5)),  // Should be boolean!
                        new AssignmentStatement("v", new ValueExpression(new IntValue(1))),
                        new AssignmentStatement("v", new ValueExpression(new IntValue(2)))));

        ArrayListRepository repo4 = new ArrayListRepository("test4.txt");
        Controller ctr4 = new Controller(repo4);
        try {
            ctr4.addNewProgram(typeError3);
            System.out.println("UNEXPECTED: Type checking passed but should have failed!\n");
        } catch (MyException e) {
            System.out.println(" Type checking correctly FAILED: " + e.getMessage() + "\n");
        }

        // Test 5: Type error - arithmetic with boolean
        System.out.println("Test 5: Arithmetic with boolean (true + 5)");
        Statement typeError4 = new PrintStatement(
                new ArithmeticExpression(
                        new ValueExpression(new BoolValue(true)),
                        new ValueExpression(new IntValue(5)),
                        '+'));

        ArrayListRepository repo5 = new ArrayListRepository("test5.txt");
        Controller ctr5 = new Controller(repo5);
        try {
            ctr5.addNewProgram(typeError4);
            System.out.println(" UNEXPECTED: Type checking passed but should have failed!\n");
        } catch (MyException e) {
            System.out.println("Type checking correctly FAILED: " + e.getMessage() + "\n");
        }

        // Test 6: Valid complex program with while
        System.out.println("Test 6: Valid while loop (int v; v=4; while(v>0) {print(v); v=v-1})");
        Statement valid2 = new CompoundStatement(
                new VariableDeclarationStatement(new IntegerType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                        new WhileStatement(
                                new RelationalExpression(
                                        new VariableExpression("v"),
                                        new ValueExpression(new IntValue(0)),
                                        ">"),
                                new CompoundStatement(
                                        new PrintStatement(new VariableExpression("v")),
                                        new AssignmentStatement("v",
                                                new ArithmeticExpression(
                                                        new VariableExpression("v"),
                                                        new ValueExpression(new IntValue(1)),
                                                        '-'))))));

        ArrayListRepository repo6 = new ArrayListRepository("test6.txt");
        Controller ctr6 = new Controller(repo6);
        try {
            ctr6.addNewProgram(valid2);
            System.out.println(" Type checking PASSED - Program is well-typed\n");
        } catch (MyException e) {
            System.out.println(" Type checking FAILED: " + e.getMessage() + "\n");
        }

        System.out.println("=== Type Checker Testing Complete ===");
    }
}
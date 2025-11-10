package view;

import controller.Controller;
import exceptions.MyException;

public class RunExampleCommand extends Command {
    private Controller controller;

    public RunExampleCommand(String key, String desc, Controller controller) {
        super(key, desc);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.executeAllSteps();
        } catch (MyException e) {
            System.out.println("Error executing program: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Runtime error: " + e.getMessage());
        }
    }
}

package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import interpreter.view.Examples;

import java.util.Objects;

public class App {
    public static void main(String[] args) {

        IStmt[] examples = { Examples.EX1, Examples.EX2, Examples.EX3 };

        int id;
        id = Integer.parseInt(args[0]);

        IStmt selectedProgram = examples[id-1];

        PrgState prgState = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                selectedProgram
        );

        IRepository repo = new Repository(prgState);
        Controller controller = new Controller(repo, true);

        try {
            System.out.println("Starting execution...");
            controller.allStep();
            System.out.println("Execution finished successfully!");
        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
        }
    }
}

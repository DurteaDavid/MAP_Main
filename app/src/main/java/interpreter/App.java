package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IStmt[] examples = { Examples.EX1, Examples.EX2, Examples.EX3 };

        while (true) {
            System.out.println("\n=== TOY LANGUAGE INTERPRETER MENU ===");
            for (int i = 0; i < examples.length; i++) {
                System.out.println((i + 1) + ". Run Example " + (i + 1));
            }
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input.trim());

                if (choice == 0) {
                    System.out.println("Exiting.");
                    break;
                }

                if (choice < 1 || choice > examples.length) {
                    System.out.println("Invalid choice! Please select a valid option from the menu.");
                    continue;
                }

                IStmt selectedProgram = examples[choice - 1];

                PrgState prgState = new PrgState(
                        new MyStack<>(),
                        new MyDictionary<>(),
                        new MyList<>(),
                        selectedProgram
                );

                IRepository repo = new Repository(prgState);
                Controller controller = new Controller(repo, true);

                System.out.println("\nStarting execution of Example " + choice + "...");
                controller.allStep();
                System.out.println("Execution finished.");

            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please enter a number.");
            } catch (Exception e) {
                System.err.println("Execution error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}

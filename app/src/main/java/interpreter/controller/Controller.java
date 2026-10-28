package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller implements IController {
    private final IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    @Override
    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty())
            throw new StatementException("execution stack is empty"); // Rule S7
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    @Override
    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        display(prg);

        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            display(prg);
        }
    }

    private void display(PrgState prg) {
        if (displayFlag) {
            System.out.println(prg);
        }
    }
}
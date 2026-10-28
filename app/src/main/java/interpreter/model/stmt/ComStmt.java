package interpreter.model.stmt;

import interpreter.model.PrgState;
import interpreter.model.adt.IStack;

public record ComStmt(IStmt first, IStmt second) implements IStmt {     // Statement rule 1
    @Override
    public PrgState execute(PrgState state) {
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return state;
    }
    @Override public String toString() { return "(" + first + "; " + second + ")"; }
}

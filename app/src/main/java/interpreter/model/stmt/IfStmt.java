package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.IValue;

public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        IValue c = cond.eval(symTable);
        if (!(c.getType() instanceof BoolType))
            throw new StatementException(c + " is not boolean!");
        var exeStack = state.getExeStack();

        if (c.toString().equals("true"))
            exeStack.push(thenS);
        else
            exeStack.push(elseS);
        return state;
    }

    @Override
    public String toString() { return "If " + cond + " then " + thenS + " else " + elseS; }
}

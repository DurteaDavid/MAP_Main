package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public record WriteHeapStmt(String varName, IExp exp) implements  IStmt{
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();
        if(!symTable.isDefined(varName))
            throw new StatementException("WriteHeap: " + varName + " is not declared!");
        if(!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("WriteHeap: " + varName + " is not a reference!");
        IValue value = exp.eval(symTable, heap);
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("WriteHeap: " + exp + " has type " + value.getType() + ", expected " + ref.locationType());
        if (!heap.isDefined(ref.address()))
            throw new StatementException("WriteHeap: " + exp + " has unallocated address!");
        heap.update(ref.address(), value);

        return state;
    }
    @Override
    public String toString() { return "wH( " + varName + ", " + exp + ")"; }
}

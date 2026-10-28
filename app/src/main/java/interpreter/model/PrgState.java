package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private final IStmt originalProgram;

    public PrgState (IStack<IStmt> exeStack, IDictionary<String,IValue> symTable, IList<IValue> out, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program;
        exeStack.push(program);
    }

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public String toString() {
        return "ProgramState {" + "\n" +
                "ExecutionStack = " + exeStack + "\n" +
                "SymbolTable = " + symTable + "\n" +
                "Out = " + out + "\n" +
                "OriginalProgram = " + originalProgram + "\n" +
                "}";
    }
}

//PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);

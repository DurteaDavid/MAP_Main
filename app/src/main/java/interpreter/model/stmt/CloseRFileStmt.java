package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var fileTable = state.getFileTable();

        if (!((exp.eval(symTable)) instanceof StringValue fileName)) {
            throw new StatementException("closeFile: " + exp + " is not a string!");
        }

        if (!fileTable.isDefined(fileName)) {
            throw new StatementException("closeFile: " + fileName + " is not open!");
        }

        BufferedReader reader = fileTable.lookup(fileName);
        try {
            reader.close();
        } catch (IOException e) {
            throw new StatementException("closeFile: cannot close file " + fileName);
        }

        fileTable.remove(fileName);
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}

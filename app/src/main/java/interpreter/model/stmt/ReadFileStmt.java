package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var fileTable = state.getFileTable();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("readFile: '" + varName + "' is not defined!");
        }

        if (!(symTable.lookup(varName).getType() instanceof IntType())) {
            throw new StatementException("readFile: '" + varName + "' is not of type int!");
        }

        if (!(exp.eval(symTable) instanceof StringValue fileName)) {
            throw new StatementException("readFile: '" + exp + "' is not a string!");
        }

        if (!fileTable.isDefined(fileName)) {
            throw new StatementException("readFile: '" + fileName + "' is not open!");
        }

        BufferedReader reader = fileTable.lookup(fileName);
        try {
            String line = reader.readLine();
            int intVal;

            if (line == null) {
                intVal = 0;
            } else {
                intVal = Integer.parseInt(line.strip());
            }

            symTable.update(varName, new IntValue(intVal));
        } catch (IOException | NumberFormatException e) {
            throw new StatementException("readFile: error reading file or parsing integer for " + fileName + ": " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}
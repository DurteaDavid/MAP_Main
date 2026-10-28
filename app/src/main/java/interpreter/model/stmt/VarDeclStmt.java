package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.type.IType;

import java.util.Objects;

public record VarDeclStmt(String name, IType type) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (symTable.isDefined(name))
            throw new StatementException("Variable " + name + " is already declared!");
        if (!Objects.equals(type.toString(), "int") && Objects.equals(type.toString(), "bool"))
            throw new StatementException("Invalid type: " + type.toString());

        symTable.put(name, type.defaultValue());
        return state;
    }

    @Override public String toString() { return type + name; }
}


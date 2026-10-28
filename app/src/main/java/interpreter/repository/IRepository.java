package interpreter.repository;

import interpreter.model.PrgState;

import java.util.List;

public interface IRepository {
    PrgState getCrtPrg();
    void add(PrgState prg);
    List<PrgState> getPrgList();
}
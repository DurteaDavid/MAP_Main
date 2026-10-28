package interpreter.repository;

import interpreter.model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> prgStates;

    public Repository(PrgState initialPrgState) {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(initialPrgState);
    }

    @Override
    public PrgState getCrtPrg() {
        return prgStates.getFirst();
    }

    @Override
    public void add(PrgState prg) {
        prgStates.add(prg);
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgStates;
    }

}
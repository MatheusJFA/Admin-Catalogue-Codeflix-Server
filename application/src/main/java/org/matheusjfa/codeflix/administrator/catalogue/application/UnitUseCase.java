package org.matheusjfa.codeflix.administrator.catalogue.application;

public abstract class UnitUseCase<IN> {
    public abstract void execute(IN command);
}

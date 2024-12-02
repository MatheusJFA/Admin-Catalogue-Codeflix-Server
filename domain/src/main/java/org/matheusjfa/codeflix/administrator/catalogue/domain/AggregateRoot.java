package org.matheusjfa.codeflix.administrator.catalogue.domain;

import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.ValidationHandler;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    protected AggregateRoot(final ID id) {
        super(id);
    }

    public abstract void validate(ValidationHandler handler);
}




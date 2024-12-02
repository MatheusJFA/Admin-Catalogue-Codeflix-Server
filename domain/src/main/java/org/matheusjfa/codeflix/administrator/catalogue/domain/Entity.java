package org.matheusjfa.codeflix.administrator.catalogue.domain;

import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.ValidationHandler;

import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected final ID id;

    protected Entity(final ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public abstract void validate(ValidationHandler handler);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
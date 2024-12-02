package org.matheusjfa.codeflix.administrator.catalogue.domain.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String id;

    public CategoryID(final String id) {
        this.id = Objects.requireNonNull(id);
    }

    public static CategoryID generate() {
        return CategoryID.from(UUID.randomUUID());
    }

    public static CategoryID from(final UUID uuid) {
        return new CategoryID(uuid.toString().toLowerCase());
    }

    public static CategoryID from(final String id) {
        return new CategoryID(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


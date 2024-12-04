package org.matheusjfa.codeflix.administrator.catalogue.domain.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String id) {
        this.value = Objects.requireNonNull(id);
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

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}


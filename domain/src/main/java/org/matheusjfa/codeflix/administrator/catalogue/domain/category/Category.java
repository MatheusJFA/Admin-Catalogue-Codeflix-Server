package org.matheusjfa.codeflix.administrator.catalogue.domain.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.AggregateRoot;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable  {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID categoryID,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(categoryID);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "'created at' must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "'updated at' must not be null");
        this.deletedAt = deletedAt;
    }

    public static Category create(
            final String name,
            final String description,
            final boolean isActive
    ) {
        final var id = CategoryID.generate();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    public static Category with(
            final CategoryID id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                id,
                name,
                description,
                isActive,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category category) {
        return with(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        final var validator = new CategoryValidator(this, handler);
        validator.validate();
    }

    public Category update(
            final String name,
            final String description,
            final boolean isActive
    ) {
        if(isActive) activate();
        else deactivate();

        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }

    public void deactivate() {
        if(getDeletedAt() == null) this.deletedAt = Instant.now();
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}


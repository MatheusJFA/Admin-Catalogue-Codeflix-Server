package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;

import java.time.Instant;

public record ListCategoriesOutput(CategoryID id,
                                   String name,
                                   String description,
                                   boolean isActive,
                                   Instant createdAt,
                                   Instant updatedAt,
                                   Instant deletedAt) {
    public static ListCategoriesOutput from(final Category category) {
        return new ListCategoriesOutput(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}

package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;

import java.time.Instant;

public record GetCategoryByIdOutput(CategoryID id,
                                String name,
                                String description,
                                boolean isActive,
                                Instant createdAt,
                                Instant updatedAt,
                                Instant deletedAt) {
    public static GetCategoryByIdOutput from(final Category category) {
        return new GetCategoryByIdOutput(category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}

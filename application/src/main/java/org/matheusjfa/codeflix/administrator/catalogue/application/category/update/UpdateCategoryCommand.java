package org.matheusjfa.codeflix.administrator.catalogue.application.category.update;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;

public record UpdateCategoryCommand(CategoryID id,
                                    String name,
                                    String description,
                                    boolean isActive) {
    public static UpdateCategoryCommand with(final CategoryID id,
                                             final String name,
                                             final String description,
                                             final boolean isActive) {
        return new UpdateCategoryCommand(id, name, description, isActive);
    }
}

package org.matheusjfa.codeflix.administrator.catalogue.application.category.update;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;

public record UpdateCategoryOutput(CategoryID id) {
    public static UpdateCategoryOutput from(final Category category) {
        return new UpdateCategoryOutput(
                category.getId()
        );
    }
}

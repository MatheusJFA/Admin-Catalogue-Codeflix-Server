package org.matheusjfa.codeflix.administrator.catalogue.application.category.delete;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;

public record DeleteCategoryOutput(String id) {
    public static DeleteCategoryOutput from(final Category category) {
        return new DeleteCategoryOutput(category.getId().getValue());
    }
}

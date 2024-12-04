package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategorySearchQuery;

public record ListCategoriesCommand(CategorySearchQuery query) {
    public static ListCategoriesCommand with(final CategorySearchQuery query) {
        return new ListCategoriesCommand(query);
    }
}

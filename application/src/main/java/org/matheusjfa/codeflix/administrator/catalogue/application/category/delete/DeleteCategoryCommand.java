package org.matheusjfa.codeflix.administrator.catalogue.application.category.delete;

import org.matheusjfa.codeflix.administrator.catalogue.application.UseCase;

public record DeleteCategoryCommand(String id) {
    public static DeleteCategoryCommand with(final String id) {
        return new DeleteCategoryCommand(id);
    }
}


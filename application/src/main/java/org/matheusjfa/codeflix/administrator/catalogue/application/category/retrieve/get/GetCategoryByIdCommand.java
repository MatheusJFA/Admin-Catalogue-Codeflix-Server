package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get;

import org.matheusjfa.codeflix.administrator.catalogue.application.UseCase;

public record GetCategoryByIdCommand(String id) {
    public static GetCategoryByIdCommand with(final String id) {
        return new GetCategoryByIdCommand(id);
    }
}


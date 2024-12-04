package org.matheusjfa.codeflix.administrator.catalogue.application.category.delete;

import org.matheusjfa.codeflix.administrator.catalogue.application.UseCase;

public abstract class DeleteCategoryUseCase extends UseCase<DeleteCategoryCommand, DeleteCategoryOutput> {
    public abstract DeleteCategoryOutput execute(DeleteCategoryCommand command);
}


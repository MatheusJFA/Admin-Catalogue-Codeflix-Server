package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get;

import org.matheusjfa.codeflix.administrator.catalogue.application.UseCase;

public abstract class GetCategoryByIdUseCase extends UseCase<GetCategoryByIdCommand, GetCategoryByIdOutput> {
    public abstract GetCategoryByIdOutput execute(GetCategoryByIdCommand command);
}


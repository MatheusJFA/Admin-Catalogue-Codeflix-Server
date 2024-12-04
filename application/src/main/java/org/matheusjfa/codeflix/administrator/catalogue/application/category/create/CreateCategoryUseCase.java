package org.matheusjfa.codeflix.administrator.catalogue.application.category.create;

import io.vavr.control.Either;
import org.matheusjfa.codeflix.administrator.catalogue.application.UseCase;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.handler.Notification;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
    public abstract Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand command);
}

package org.matheusjfa.codeflix.administrator.catalogue.application.category.update;

import io.vavr.control.Either;
import org.matheusjfa.codeflix.administrator.catalogue.application.UseCase;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.handler.Notification;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> { }


package org.matheusjfa.codeflix.administrator.catalogue.application.category.create;

import io.vavr.API;
import io.vavr.control.Either;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{
    private CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }


    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();

        final var notification = Notification.create();

        final var category = Category.create(name, description, isActive);

        category.validate(notification);

        return notification.hasErrors()
                ? Either.left(notification)
                : create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(final Category category) {
        return API.Try(() -> gateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}

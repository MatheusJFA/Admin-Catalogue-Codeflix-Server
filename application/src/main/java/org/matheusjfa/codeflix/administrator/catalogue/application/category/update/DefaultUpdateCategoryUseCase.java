package org.matheusjfa.codeflix.administrator.catalogue.application.category.update;

import io.vavr.API;
import io.vavr.control.Either;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
    private CategoryGateway gateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand command) {
        final var id = command.id();
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();

        final var category = this.gateway.findById(id)
                .map(c -> c.update(name, description, isActive))
                .orElseThrow(categoryNotFound(id));

        final var notification = Notification.create();

        category.update(name, description, isActive)
                .validate(notification);

        return notification.hasErrors() ? Either.left(notification) : update(category);

    }

    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return API.Try(() -> this.gateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<DomainException> categoryNotFound(final CategoryID id) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(id.getValue()))
        );
    }
}

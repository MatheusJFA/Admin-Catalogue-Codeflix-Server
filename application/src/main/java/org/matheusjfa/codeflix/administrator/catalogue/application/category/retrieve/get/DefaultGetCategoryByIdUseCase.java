package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private CategoryGateway gateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public GetCategoryByIdOutput execute(GetCategoryByIdCommand command) {
        final var id = CategoryID.from(command.id());

        return this.gateway.findById(id)
                .map(GetCategoryByIdOutput::from)
                .orElseThrow(categoryNotFound(id));
    }

    private Supplier<DomainException> categoryNotFound(final CategoryID id) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(id.getValue()))
        );
    }
}

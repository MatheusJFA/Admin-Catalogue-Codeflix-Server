package org.matheusjfa.codeflix.administrator.catalogue.application.category.delete;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {
    private final CategoryGateway gateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public DeleteCategoryOutput execute(DeleteCategoryCommand command) {
        final var id = CategoryID.from(command.id());

        final var category = gateway.findById(id)
                .orElseThrow(categoryNotFound(id));

        final var deletedCategory = gateway.deleteById(category.getId());

        return DeleteCategoryOutput.from(deletedCategory);
    }

    private Supplier<DomainException> categoryNotFound(final CategoryID id) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(id.getValue()))
        );
    }

}

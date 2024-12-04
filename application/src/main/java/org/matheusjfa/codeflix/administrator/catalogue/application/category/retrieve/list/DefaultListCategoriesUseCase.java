package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {
    private CategoryGateway gateway;

    public DefaultListCategoriesUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ListCategoriesOutput> execute(ListCategoriesCommand command) {
        final var query = command.query();

        return this.gateway.findAll(query)
                .map(ListCategoriesOutput::from);
    }
}

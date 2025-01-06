package org.matheusjfa.codeflix.administrator.catalogue.domain.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);

    Optional<Category> findById(CategoryID id);

    Pagination<Category> findAll(CategorySearchQuery query);

    Category update(Category category);

    Category deleteById(CategoryID id);
}

package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategorySearchQuery;
import org.matheusjfa.codeflix.administrator.catalogue.domain.pagination.Pagination;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;

import java.util.Objects;
import java.util.Optional;

public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Category create(Category category) {
        return null;
    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public Category deleteById(CategoryID id) {
        return null;
    }
}

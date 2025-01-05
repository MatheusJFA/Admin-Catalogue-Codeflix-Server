package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategorySearchQuery;
import org.matheusjfa.codeflix.administrator.catalogue.domain.pagination.Pagination;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.Optional;

public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Category create(final Category category) {
        return this.save(category);
    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return this.repository.findById(id.getValue())
                .map(CategoryJPAEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(terms -> !terms.isEmpty())
                .map(term -> SpecificationUtils
                            .<CategoryJPAEntity>like("name", term)
                            .or(SpecificationUtils.<CategoryJPAEntity>like("description", term)))
                .orElse(null);


        final var result = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.map(CategoryJPAEntity::toAggregate).toList()
        );
    }

    @Override
    public Category update(final Category category) {
        return this.save(category);
    }

    @Override
    public Category deleteById(final CategoryID id) {
        final var entity = findById(id)
                .orElseThrow();

        this.repository.deleteById(id.getValue());
        return Category.with(entity);
    }

    private Category save(final Category category) {
        final var entity = CategoryJPAEntity.from(category);
        return this.repository
                .save(entity)
                .toAggregate();
    }
}

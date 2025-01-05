package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {
    Page<CategoryJPAEntity> findAll(Specification<CategoryJPAEntity> where, Pageable page);
}

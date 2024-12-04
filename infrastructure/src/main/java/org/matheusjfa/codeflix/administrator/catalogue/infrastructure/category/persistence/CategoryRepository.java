package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntityJPA, String> {
}

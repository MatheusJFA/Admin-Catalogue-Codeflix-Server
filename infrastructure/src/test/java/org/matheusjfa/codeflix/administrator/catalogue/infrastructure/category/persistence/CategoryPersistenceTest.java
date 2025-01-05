package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.MySQLGatewayTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class  CategoryPersistenceTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAnInvalidCategoryWithNullName_whenCallsSave_shouldReturnAnError() {
        // Arrange
        final var expectedProperty = "name";
        final var expectedMessage = "not-null property references a null or transient value : org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity.name";
        final var category = Category.create("Category", "Category Description", true);
        // Act
        final var entity = CategoryJPAEntity.from(category);
        entity.setName(null);

        // Assert
        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(entity));

        final var cause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedProperty, cause.getPropertyName());
        Assertions.assertEquals(expectedMessage, cause.getMessage());
    }

    @Test
    public void givenAnInvalidCategoryWithNullCreatedAt_whenCallsSave_shouldReturnAnError() {
        // Arrange
        final var expectedProperty = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity.createdAt";
        final var category = Category.create("Category", "Category Description", true);
        // Act
        final var entity = CategoryJPAEntity.from(category);
        entity.setCreatedAt(null);

        // Assert
        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(entity));

        final var cause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedProperty, cause.getPropertyName());
        Assertions.assertEquals(expectedMessage, cause.getMessage());
    }

    @Test
    public void givenAnInvalidCategoryWithNullUpdatedAt_whenCallsSave_shouldReturnAnError() {
        // Arrange
        final var expectedProperty = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity.updatedAt";
        final var category = Category.create("Category", "Category Description", true);
        // Act
        final var entity = CategoryJPAEntity.from(category);
        entity.setUpdatedAt(null);

        // Assert
        final var exception = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(entity));

        final var cause = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());

        Assertions.assertEquals(expectedProperty, cause.getPropertyName());
        Assertions.assertEquals(expectedMessage, cause.getMessage());
    }
}


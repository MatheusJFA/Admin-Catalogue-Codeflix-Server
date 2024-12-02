package org.matheusjfa.codeflix.administrator.catalogue.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.handler.ThrowsValidationHandler;

public class CategoryTest {
    /** Test Scenarios
     * 1) Should create a category
     * 2) Shouldn't create a category with null name
     * 3) Shouldn't create a category with blank name
     * 4) Shouldn't create a category with name less than 3 characters
     * 5) Shouldn't create a category with name greater than 255 characters
     * 6) Should create a category with empty description
     * 7) Should create a category with inactive status
     * 8) Should activate a category with inactive status
     * 9) Should deactivate a category with active status
     * 10) Should update a category
     * 11) Should update a category with inactive status
     * 12) Should update a valid category to invalid without throwing an exception
     * */

    @Test
    public void givenAValidCategory_whenCreateCategory_thenShouldCreateCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        // Act
        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenANullName_whenCreateCategory_thenShouldThrowAnException() {
        // Arrange
        final String expectedName = null;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        final var handler = new ThrowsValidationHandler();

        // Act
        final var exception = Assertions.assertThrows(DomainException.class,
                () -> category.validate(handler));

        // Assert
        Assertions.assertEquals("'name' should not be null", exception.getMessage());
        Assertions.assertNotNull(exception.getErrors());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenABlankName_whenCreateCategory_thenShouldThrowAnException() {
        // Arrange
        final String expectedName = " ";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessage = "'name' should not be blank";
        final var expectedErrorCount = 1;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        final var handler = new ThrowsValidationHandler();

        // Act
        final var exception = Assertions.assertThrows(DomainException.class,
                () -> category.validate(handler));

        // Assert
        Assertions.assertNotNull(exception.getErrors());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenANameLessThan3Characters_whenCreateCategory_thenShouldThrowAnException() {
        // Arrange
        final String expectedName = "Ca";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessage = "'name' should have at least 3 characters";
        final var expectedErrorCount = 1;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        final var handler = new ThrowsValidationHandler();

        // Act
        final var exception = Assertions.assertThrows(DomainException.class,
                () -> category.validate(handler));

        // Assert
        Assertions.assertNotNull(exception.getErrors());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenANameGreaterThan255Characters_whenCreateCategory_thenShouldThrowAnException() {
        // Arrange
        final String expectedName = "Category".repeat(51);
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessage = "'name' should have at most 255 characters";
        final var expectedErrorCount = 1;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        final var handler = new ThrowsValidationHandler();

        // Act
        final var exception = Assertions.assertThrows(DomainException.class,
                () -> category.validate(handler));

        // Assert
        Assertions.assertNotNull(exception.getErrors());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnEmptyDescription_whenCreateCategory_thenShouldCreateCategory() {
        // Arrange
        final var expectedName = "Category";
        final String expectedDescription = "";
        final var expectedIsActive = true;

        // Act
        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenAnInactiveCategory_whenCreateCategory_thenShouldCreateCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;

        // Act
        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenAnInactiveCategory_whenActivateCategory_thenShouldActivateCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Act
        category.activate();

        // Assert
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
    }

    @Test
    public void givenAnActiveCategory_whenDeactivateCategory_thenShouldDeactivateCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Act
        category.deactivate();

        // Assert
        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
    }

    @Test
    public void givenAValidCategory_whenUpdateCategory_thenShouldUpdateCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var category = Category.create("Test Category", "Test Category Description", true);

        final var updatedAt = category.getUpdatedAt();

        // Act
        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getUpdatedAt());

        Assertions.assertTrue(updatedAt.isBefore(updatedCategory.getUpdatedAt()));
    }

    @Test
    public void givenAValidCategory_whenUpdateInactiveCategory_thenShouldUpdateCategory() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", false);

        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var updatedAt = category.getUpdatedAt();

        // Act
        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());

        Assertions.assertTrue(updatedAt.isBefore(updatedCategory.getUpdatedAt()));
    }

    @Test
    public void givenAValidCategory_whenUpdateValidToInvalidCategory_thenShouldUpdateCategory() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);

        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;

        final var updatedAt = category.getUpdatedAt();

        // Act
        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
        Assertions.assertTrue(updatedAt.isBefore(updatedCategory.getUpdatedAt()));
    }
}

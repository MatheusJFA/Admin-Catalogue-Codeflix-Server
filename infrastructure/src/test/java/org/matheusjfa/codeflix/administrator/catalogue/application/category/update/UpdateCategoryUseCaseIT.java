package org.matheusjfa.codeflix.administrator.catalogue.application.category.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.matheusjfa.codeflix.administrator.catalogue.IntegrationTest;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenShouldUpdateCategory() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);

        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        // Act
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());


        final var result = Assertions.assertDoesNotThrow(() -> useCase.execute(command).get());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());
        Assertions.assertEquals(expectedId.getValue(), result.id().getValue());

        final var entity = repository.findById(result.id().getValue()).get();

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(expectedId.getValue(), entity.getId());
        Assertions.assertEquals(expectedName, entity.getName());
        Assertions.assertEquals(expectedDescription, entity.getDescription());
        Assertions.assertEquals(expectedIsActive, entity.isActive());

        // Check if the category was updated and didn't create a new one
        Assertions.assertEquals(1, repository.count());

    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateCategory_thenShouldThrowAnException() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);

        final String expectedName = null;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var expectedMessageError = "'name' should not be null";
        final var expectedErrorCount = 1;

        // Act
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());


        final var exception = Assertions.assertDoesNotThrow(() -> useCase.execute(command).getLeft());

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessageError, exception.getErrors().get(0).message());

        // Check if the category was updated and didn't create a new one
        Assertions.assertEquals(1, repository.count());
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateInactiveCategory_thenShouldUpdateCategory() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);

        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;
        final var expectedId = category.getId();

        // Act
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());


        final var result = Assertions.assertDoesNotThrow(() -> useCase.execute(command).get());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());
        Assertions.assertEquals(expectedId.getValue(), result.id().getValue());

        final var entity = repository.findById(result.id().getValue()).get();

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(expectedId.getValue(), entity.getId());
        Assertions.assertEquals(expectedName, entity.getName());
        Assertions.assertEquals(expectedDescription, entity.getDescription());
        Assertions.assertEquals(expectedIsActive, entity.isActive());

        Assertions.assertNotNull(entity.getDeletedAt());

        // Check if the category was updated and didn't create a new one
        Assertions.assertEquals(1, repository.count());
    }

    @Test
    public void givenAValidCommand_whenRepositoryThrows_thenShouldReturnError() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);

        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(category.getId(), "Category", "Category Description", true);

        // Act

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());

        Mockito.doThrow(DomainException.with(new Error(expectedErrorMessage)))
                .when(gateway)
                .findById(Mockito.any());

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command).getLeft());

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFound() {
        // Arrange
        final var invalidID = CategoryID.from("invalid-id");

        final var expectedErrorMessage = "Category with ID %s was not found".formatted(invalidID.getValue());
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(invalidID, "Category", "Category Description", true);
        // Act

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command).getLeft());

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

}

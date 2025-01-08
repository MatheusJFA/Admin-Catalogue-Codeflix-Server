package org.matheusjfa.codeflix.administrator.catalogue.application.category.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.matheusjfa.codeflix.administrator.catalogue.IntegrationTest;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCategory_whenCallsCreate_thenShouldCreateCategory() {
        // Arrange
        final var expectedName = "Category 1";
        final var expectedDescription = "Category 1 description";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertEquals(0, repository.count());

        final var result = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());

        Assertions.assertEquals(1, repository.count());

        // Check if the category was created
        final var category = repository
                .findById(result.id().getValue())
                .get();

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenAInactiveCategory_whenCallsCreate_thenShouldCreateCategory() {
        // Arrange
        final var expectedName = "Category 1";
        final var expectedDescription = "Category 1 description";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertEquals(0, repository.count());

        final var result = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());

        Assertions.assertEquals(1, repository.count());

        // Check if the category was created
        final var category = repository
                .findById(result.id().getValue())
                .get();

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenANullDescription_whenCallsCreate_thenShouldCreateCategory() {
        // Arrange
        final var expectedName = "Category 1";
        final String expectedDescription = null;
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertEquals(0, repository.count());

        final var result = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());

        Assertions.assertEquals(1, repository.count());

        // Check if the category was created
        final var category = repository
                .findById(result.id().getValue())
                .get();

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenAnInvalidCategory_whenCallsCreate_thenShouldReturnAnError() {
        // Arrange
        final String expectedName = null;
        final var expectedDescription = "Category 1 description";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertEquals(0, repository.count());

        final var exception = useCase.execute(command).getLeft();

        // Assert
        Assertions.assertNotNull(exception);

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Assertions.assertEquals(0, repository.count());
        Mockito.verify(gateway, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAValidCategory_whenGatewayThrowsAnException_thenShouldReturnAnError() {
        // Arrange
        final var expectedName = "Category 1";
        final var expectedDescription = "Category 1 description";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "An error occurred while creating the category";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new RuntimeException(expectedErrorMessage))
                .when(gateway)
                .create(Mockito.any());

        // Act
        Assertions.assertEquals(0, repository.count());

        final var exception = useCase.execute(command).getLeft();

        // Assert
        Assertions.assertNotNull(exception);

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        // Check if the category was not created
        Assertions.assertEquals(0, repository.count());
    }

}

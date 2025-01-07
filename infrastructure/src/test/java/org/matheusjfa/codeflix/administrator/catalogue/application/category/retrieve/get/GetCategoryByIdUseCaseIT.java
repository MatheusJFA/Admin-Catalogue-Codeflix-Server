package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get;

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
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCategoryId_whenCallsGetCategoryById_thenShouldReturnCategory() {
        // Arrange
        final var expecetedName = "Category 1";
        final var expectedDescription = "Category 1 description";
        final var expectedIsActive = true;

        final var category = Category.create("Category 1", "Category 1 description", true);

        final var id = category.getId();

        final var command = GetCategoryByIdCommand.with(id.getValue());

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());

        // Act

        final var result = Assertions.assertDoesNotThrow(() -> useCase.execute(command));

        // Assert

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());

        Assertions.assertEquals(id.getValue(), result.id().getValue());
        Assertions.assertEquals(expecetedName, result.name());
        Assertions.assertEquals(expectedDescription, result.description());
        Assertions.assertEquals(expectedIsActive, result.isActive());
    }

    @Test
    public void givenAnInvalidCategoryId_whenCallsGetCategoryById_thenShouldThrowException() {
        // Arrange
        final var id = CategoryID.from("invalid-id");
        final var command = GetCategoryByIdCommand.with(id.getValue());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(id.getValue());

        // Act
        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
    }

    @Test
    public void givenAValidCategoryId_whenGatewayThrowsException_thenShouldThrowException() {
        // Arrange
        final var category = Category.create("Category 1", "Category 1 description", true);
        final var id = category.getId();

        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var command = GetCategoryByIdCommand.with(id.getValue());

        // Act
        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());

        // Act
        Mockito.doThrow(DomainException.with(new Error(expectedErrorMessage)))
                .when(gateway)
                .findById(Mockito.any());

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
}

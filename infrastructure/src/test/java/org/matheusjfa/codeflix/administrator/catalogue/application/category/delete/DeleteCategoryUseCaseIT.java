package org.matheusjfa.codeflix.administrator.catalogue.application.category.delete;

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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCategory_whenCallsDelete_thenShouldDeleteCategory() {
        // Arrange
        final var category = Category.create("Category 1", "Category 1 description", true);
        final var id = category.getId();

        final var command = DeleteCategoryCommand.with(id.getValue());
        // Act

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());

        final var result = Assertions.assertDoesNotThrow(() -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());
        Assertions.assertEquals(id.getValue(), result.id());

        // Check if the category was deleted
        Assertions.assertEquals(0, repository.count());

    }

    @Test
    public void givenAInvalidID_whenCallsDelete_thenShouldNotDeleteCategory() {
        // Arrange
        final var id = CategoryID.from("invalid-id");

        final var expectedMessageError = "Category with ID %s was not found".formatted(id.getValue());
        final var expectedErrorCount = 1;

        final var command = DeleteCategoryCommand.with(id.getValue());

        // Act
        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedMessageError, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
    }

    @Test
    public void givenAValidCategory_whenGatewayThrowsException_thenShouldNotDeleteCategory() {
        // Arrange
        final var category = Category.create("Category 1", "Category 1 description", true);
        final var id = category.getId();

        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var command = DeleteCategoryCommand.with(id.getValue());

        // Act

        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        this.repository.saveAndFlush(CategoryJPAEntity.from(category));

        // Check if the category was created
        Assertions.assertEquals(1, repository.count());

        Mockito.doThrow(DomainException.with(new Error(expectedErrorMessage)))
                .when(gateway)
                .findById(Mockito.any());


        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

        // Check if the category was not deleted
        Assertions.assertEquals(1, repository.count());
    }
}

package org.matheusjfa.codeflix.administrator.catalogue.application.retrieve.get;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get.GetCategoryByIdCommand;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {
    /**
     * Test Scenarios
     * 1) Retrieve a category when passing a valid ID
     * 2) Throw an exception when passing an invalid ID
     * 3) Gateway throws an exception when trying to retrieve a category
     * */

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidId_whenCallGetCategory_shouldReturnCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var category = Category.create("Category", "Category Description", true);
        final var expectedId = category.getId();

        final var command = GetCategoryByIdCommand.with(expectedId.getValue());

        // Act
        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(category.clone()));

        final var output = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedIsActive, output.isActive());
    }

    @Test
    public void givenAnInvalidId_whenCallGetCategory_shouldThrowException() {
        // Arrange
        final var expectedId = CategoryID.from("invalid-id");
        final var expectedMessageError = "Category with ID %s was not found".formatted(expectedId.getValue());

        final var command = GetCategoryByIdCommand.with(expectedId.getValue());

        // Act
        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedMessageError, exception.getMessage());
    }

    @Test
    public void givenAValidId_whenCallGetCategory_shouldThrowException() {
        // Arrange
        final var expectedId = CategoryID.from("valid-id");
        final var expectedMessageError = "Error on retrieving category";

        final var command = GetCategoryByIdCommand.with(expectedId.getValue());

        // Act
        Mockito.when(gateway.findById(Mockito.any()))
                .thenThrow(new RuntimeException(expectedMessageError));

        final var exception = Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedMessageError, exception.getMessage());
    }
}

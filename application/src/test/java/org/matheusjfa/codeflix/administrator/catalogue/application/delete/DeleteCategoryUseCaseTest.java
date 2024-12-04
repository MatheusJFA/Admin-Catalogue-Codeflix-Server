package org.matheusjfa.codeflix.administrator.catalogue.application.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.delete.DefaultDeleteCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.delete.DeleteCategoryCommand;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    /**
     * Test Scenarios
     * 1) Delete a category when passing a valid ID
     * 2) Throw an exception when passing an invalid ID
     * 3) Gateway throws an exception when trying to delete a category
     * */

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidID_whenCallsDeleteCategory_thenShouldDeleteCategory() {
        // Arrange
        final var category = Category.create("Category", "Category Description", true);
        final var id = category.getId();

        // Act
        final var command = DeleteCategoryCommand.with(id.getValue());

        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(category.clone()));

        Mockito.when(gateway.deleteById(Mockito.any()))
                .thenReturn(category);

        final var output = Assertions.assertDoesNotThrow(() -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1))
                .deleteById(Mockito.any());
    }

    @Test
    public void givenAnInvalidID_whenCallsDeleteCategory_thenShouldThrowAnException() {
        // Arrange
        final var invalidID = CategoryID.from("invalid-id");

        final var expectedMessageError = "Category with ID %s was not found".formatted(invalidID.getValue());
        final var expectedErrorCount = 1;

        final var command = DeleteCategoryCommand.with(invalidID.getValue());

        // Act
        Mockito.when(gateway.findById(Mockito.any())).thenThrow(DomainException.with(new Error(expectedMessageError)));

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessageError, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidID_whenCallsDeleteCategoryThrows_thenShouldReturnAnException() {
        // Arrange
        final var category = Category.create("Category", "Category Description", true);
        final var id = category.getId();

        final var expectedMessageError = "Error on deleting category";
        final var expectedErrorCount = 1;

        final var command = DeleteCategoryCommand.with(id.getValue());

        // Act
        Mockito.when(gateway.findById(Mockito.any()))
                .thenReturn(Optional.of(category.clone()));

        Mockito.when(gateway.deleteById(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedMessageError)));

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessageError, exception.getErrors().get(0).message());

        Mockito.verify(gateway, Mockito.times(1))
                .deleteById(Mockito.eq(id));
    }
}

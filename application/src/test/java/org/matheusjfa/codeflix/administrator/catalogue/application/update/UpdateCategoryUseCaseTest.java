package org.matheusjfa.codeflix.administrator.catalogue.application.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.update.DefaultUpdateCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.update.UpdateCategoryCommand;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    /**
     * Test Scenarios
     * 1) Update a category when passing valid params
     * 2) Throw an exception when passing invalid params
     * 3) Update an inactive category when passing valid params
     * 4) Repository throws an exception when trying to update a category
     * 5) Throws an exception when trying to update a not found category
     * */

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway repository;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

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

        Mockito.when(repository.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category.clone()));
        Mockito.when(repository.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(repository, Mockito.times(1)).update(Mockito.argThat(
                updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateCategory_thenShouldThrowAnException() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);
        final var invalidID = category.getId();
        final String expectedName = null;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessageError = "'name' should not be null";
        final var expectedErrorCount = 1;

        // Act
        final var command = UpdateCategoryCommand.with(invalidID, expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(invalidID)))
                .thenReturn(Optional.of(category.clone()));

        final var notification = useCase.execute(command).getLeft();
        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedMessageError, notification.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(invalidID));
        Mockito.verify(repository, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateInactiveCategory_thenShouldUpdateCategory() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", false);

        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        // Act
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category.clone()));
        Mockito.when(repository.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(repository, Mockito.times(1)).update(Mockito.argThat(
                updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenRepositoryThrows_thenShouldReturnError() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);
        final var expectedId = category.getId();
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessageError = "Error on updating category";
        final var expectedErrorCount = 1;

        // Act
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(category.clone()));
        Mockito.when(repository.update(Mockito.any())).thenThrow(new RuntimeException(expectedMessageError));

        final var notification = useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedMessageError, notification.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(repository, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFound() {
        // Arrange
        final var category = Category.create("Test Category", "Test Category Description", true);
        final var invalidID = category.getId();
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedMessageError = "Category with ID %s was not found".formatted(invalidID.getValue());
        final var expectedErrorCount = 1;

        // Act
        final var command = UpdateCategoryCommand.with(invalidID, expectedName, expectedDescription, expectedIsActive);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command).getLeft());

        // Assert
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedMessageError, exception.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(invalidID));
        Mockito.verify(repository, Mockito.times(0)).update(Mockito.any());
    }
}
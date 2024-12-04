package org.matheusjfa.codeflix.administrator.catalogue.application.create;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.create.CreateCategoryCommand;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.create.DefaultCreateCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    /**
     * Test Scenarios
     * 1) Create a new category when passing valid params and returning the category ID
     * 2) Create a new category when passing invalid params
     * 3) Create a new inactive category when passing valid params and returning the category ID
     * 4) Create a new category but the gateway throws an exception
     * */

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryID() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        // Act
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.nonNull(category.getId())
                                && Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldThrowException() {
        // Arrange
        final String expectedName = null;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var notification = useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateInactiveCategory_thenShouldReturnCategoryID() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = false;

        // Act
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.nonNull(category.getId())
                                && Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.nonNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategoryButGatewayThrowsException_thenShouldThrowException() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Error on creating category";

        // Act
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.nonNull(category.getId())
                                && Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }


}

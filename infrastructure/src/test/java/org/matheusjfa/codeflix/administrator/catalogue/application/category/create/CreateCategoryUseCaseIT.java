package org.matheusjfa.codeflix.administrator.catalogue.application.category.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.matheusjfa.codeflix.administrator.catalogue.IntegrationTest;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

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
}

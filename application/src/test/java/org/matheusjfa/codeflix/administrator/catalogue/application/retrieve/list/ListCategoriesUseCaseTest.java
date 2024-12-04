package org.matheusjfa.codeflix.administrator.catalogue.application.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list.DefaultListCategoriesUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list.ListCategoriesCommand;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list.ListCategoriesOutput;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategorySearchQuery;
import org.matheusjfa.codeflix.administrator.catalogue.domain.pagination.Pagination;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    /**
     * Test Scenarios
     * 1) Retrieve all categories with pagination
     * 2) Retrieve an empty list of categories
     * 3) Throws an exception when trying to retrieve all categories
     */

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway repository;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

    private List<Category> createCategories() {
        return List.of(
                Category.create("Category 1", "Category 1 Description", true),
                Category.create("Category 2", "Category 2 Description", true),
                Category.create("Category 3", "Category 3 Description", false),
                Category.create("Category 4", "Category 4 Description", true),
                Category.create("Category 5", "Category 5 Description", false),
                Category.create("Category 6", "Category 6 Description", true),
                Category.create("Category 7", "Category 7 Description", false),
                Category.create("Category 8", "Category 8 Description", false),
                Category.create("Category 9", "Category 9 Description", true),
                Category.create("Category 10", "Category 10 Description", true)
        );
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var categories = createCategories();
        final var expectedCategories = categories.stream().map(ListCategoriesOutput::from).toList();

        final var expectedItemsCount = categories.size();
        // Act
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var command = ListCategoriesCommand.with(query);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedItemsCount, categories);

        Mockito.when(repository.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var output = useCase.execute(command);

        // Assert
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedItemsCount, output.total());
        Assertions.assertEquals(expectedCategories, output.items());
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnAnEmptyList() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 0;

        // Act
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var command = ListCategoriesCommand.with(query);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedItemsCount, List.<Category>of());

        Mockito.when(repository.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var output = useCase.execute(command);

        // Assert
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedItemsCount, output.total());
        Assertions.assertTrue(output.items().isEmpty());
    }

    @Test
    public void givenAValidParams_whenRepositoryThrowsAnException_thenShouldThrowAnException() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedMessageError = "Error on retrieving categories";

        // Act
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var command = ListCategoriesCommand.with(query);

        Mockito.when(repository.findAll(Mockito.any())).thenThrow(new RuntimeException(expectedMessageError));

        final var exception = Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedMessageError, exception.getMessage());
    }


}

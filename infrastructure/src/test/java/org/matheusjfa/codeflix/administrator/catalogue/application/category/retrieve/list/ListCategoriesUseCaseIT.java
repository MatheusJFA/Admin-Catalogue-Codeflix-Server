package org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.matheusjfa.codeflix.administrator.catalogue.IntegrationTest;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategorySearchQuery;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @BeforeEach
    void PopulateCategories() {
        this.createCategories();
    }

    @Test
    public void givenAValidCommand_whenCallsListCategories_thenShouldReturnCategories() {
        // Arrange
        final var expectedPage = 0;
        final var expectedSize = 3;
        final var expectedTotalElements = 8;
        final var expectedTotalPages = 3;

        final var query = new CategorySearchQuery(0, 3, "", "name", "asc");
        final var command = ListCategoriesCommand.with(query);

        // Act
        final var result = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedSize, result.perPage());
        Assertions.assertEquals(expectedTotalElements, result.total());
        Assertions.assertEquals(expectedTotalPages, result.items().size());

        final var firstElement = result.items().get(0);

        Assertions.assertEquals("Animes", firstElement.name());
        Assertions.assertEquals("Anime Category", firstElement.description());
        Assertions.assertTrue(firstElement.isActive());
    }

    @ParameterizedTest
    @CsvSource({
        "asc, 0, 3, 1, 1, Ani",
        "desc, 0, 3, 1, 1, Top",
        "asc, 0, 3, 1, 1, Doc",
        "desc, 0, 3, 1, 1, Ah",
        "asc, 0, 3, 1, 1, Car",
        "asc, 0, 3, 3, 7, Cat",
        "asc, 2, 3, 0, 0, Invalid",
    })
    public void givenAValidCommandSortingByDescription_whenCallsListCategories_thenShouldReturnCategories(
            final String expectedSortDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedTotalElements,
            final int expectedTotal,
            final String expectedTerms
    ) {
        // Arrange
        final String expectedSortBy = "description";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSortBy, expectedSortDirection);
        final var command = ListCategoriesCommand.with(query);

        // Act
        final var result = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotalElements, result.items().size());
        Assertions.assertEquals(expectedTotal, result.total());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 3, 3, 8, ''",
            "0, 3, 1, 1, 'Horror Movies!'",
            "0, 3, 0, 0, 'Invalid'",
            "0, 3, 2, 2, 'Movies'",
            "0, 3, 1, 1, 'Series'",
            "1, 3, 0, 1, 'Documentary'",
    })
    public void givenAValidCommandFilteringByName_whenCallsListCategories_thenShouldReturnCategories(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedTotalElements,
            final int expectedTotal,
            final String expectedTerms
    ) {
        // Arrange
        final var expectedSortBy = "name";
        final var expectedSortDirection = "asc";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSortBy, expectedSortDirection);
        final var command = ListCategoriesCommand.with(query);
        // Act
        final var result = useCase.execute(command);
        System.out.println(result);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTotalElements, result.items().size());
        Assertions.assertEquals(expectedTotal, result.total());
    }

    @Test
    public void givenAValidCommandFilteringByDescription_whenCallsListCategories_thenShouldReturnCategories() {
        // Arrange
        final var expectedPage = 0;
        final var expectedSize = 3;
        final var expectedTotalElements = 1;
        final var expectedTotalPages = 1;

        Assertions.assertEquals(0, this.repository.count());

        this.createCategories();

        Assertions.assertEquals(8, this.repository.count());

        final var query = new CategorySearchQuery(0, 3, "Ah, Horror Movies!", "description", "asc");
        final var command = ListCategoriesCommand.with(query);
        // Act
        final var result = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedSize, result.perPage());
        Assertions.assertEquals(expectedTotalElements, result.total());
        Assertions.assertEquals(expectedTotalPages, result.items().size());

        final var firstElement = result.items().get(0);

        Assertions.assertEquals("Horror Movies!", firstElement.name());
        Assertions.assertEquals("Ah, Horror Movies!", firstElement.description());
        Assertions.assertTrue(firstElement.isActive());
    }

    @Test
    public void givenAInvalidQueryTermToSearch_whenCallsListCategories_thenShouldReturnEmptyListOfCategories() {
        // Arrange
        final var expectedPage = 0;
        final var expectedSize = 3;
        final var expectedTotalElements = 0;
        final var expectedTotalPages = 0;

        Assertions.assertEquals(0, this.repository.count());

        this.createCategories();

        Assertions.assertEquals(8, this.repository.count());

        final var query = new CategorySearchQuery(0, 3, "Invalid", "name", "asc");
        final var command = ListCategoriesCommand.with(query);
        // Act
        final var result = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedSize, result.perPage());
        Assertions.assertEquals(expectedTotalElements, result.total());
        Assertions.assertEquals(expectedTotalPages, result.items().size());
    }

    private void createCategories() {
        final var categories = List.of(
                Category.create("Movies", "Movies Category", true),
                Category.create("Series", "Series Category", false),
                Category.create("Documentary", "Documentary Category", true),
                Category.create("Animes", "Anime Category", true),
                Category.create("Cartoon", "Cartoons Category", false),
                Category.create("Most Watched", "Most Watched Category", true),
                Category.create("Top 10", "Top 10 Category", true),
                Category.create("Horror Movies!", "Ah, Horror Movies!", true)
        );

        this.repository.saveAll(categories
                .stream()
                .map(CategoryJPAEntity::from)
                .toList()
        );
    }
}

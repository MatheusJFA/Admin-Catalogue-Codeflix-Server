package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category;

import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.Category;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryID;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategorySearchQuery;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.MySQLGatewayTest;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryJPAEntity;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway gateway;

    @Autowired
    private CategoryRepository repository;

    // Create
    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Act
        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        // Call the gateway
        final var result = this.gateway.create(category);

        // Check if the database has one entity
        Assertions.assertEquals(1, repository.count());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getId(), result.getId());
        Assertions.assertEquals(category.getName(), result.getName());
        Assertions.assertEquals(category.getDescription(), result.getDescription());
        Assertions.assertEquals(category.isActive(), result.isActive());

        Assertions.assertEquals(category.getCreatedAt(), result.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), result.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), result.getDeletedAt());

        // Check if the entity was saved correctly in the database
        final var entity = this.gateway.findById(result.getId()).get();

        Assertions.assertEquals(category.getId(), entity.getId());
        Assertions.assertEquals(category.getName(), entity.getName());
        Assertions.assertEquals(category.getDescription(), entity.getDescription());
        Assertions.assertEquals(category.isActive(), entity.isActive());

        Assertions.assertEquals(category.getCreatedAt(), entity.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), entity.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), entity.getDeletedAt());
    }


    // Update
    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnACategoryUpdated() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var expectedOriginalCategory = "Category II";
        final String expectedOriginalDescription = null;
        final var expectedOriginalIsActive = false;

        final var category = Category.create(expectedOriginalCategory, expectedOriginalDescription, expectedOriginalIsActive);

        // Act
        // Check if the database is empty
        Assertions.assertEquals(0, repository.count());

        // Save the entity in the database
        // Flush is used to force the database to save the entity
        this.gateway.create(category);

        // Check if the database has one entity
        Assertions.assertEquals(1, this.repository.count());

        final var originalCategory = this.gateway.findById(category.getId()).orElseThrow();

        Assertions.assertEquals(expectedOriginalCategory, originalCategory.getName());
        Assertions.assertEquals(expectedOriginalDescription, originalCategory.getDescription());
        Assertions.assertEquals(expectedOriginalIsActive, originalCategory.isActive());

        Assertions.assertEquals(1, this.repository.count());

        final var updatedCategory = category
                .clone()
                .update(expectedName, expectedDescription, expectedIsActive);

        final var result = this.gateway.update(updatedCategory);

        // Check if the database has one entity and did not create a new one
        Assertions.assertEquals(1, repository.count());

        // Assert
        // Check if the entity was updated correctly
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getId(), result.getId());
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedDescription, result.getDescription());
        Assertions.assertEquals(expectedIsActive, result.isActive());

        Assertions.assertEquals(category.getCreatedAt(), result.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(result.getUpdatedAt()));
        Assertions.assertNotEquals(category.getDeletedAt(), result.getDeletedAt());

        // Check if the entity was saved correctly in the database
        final var entity = this.gateway.findById(result.getId()).get();

        // Check if all the fields were updated correctly
        Assertions.assertEquals(category.getId().getValue(), entity.getId().getValue());
        Assertions.assertEquals(expectedName, entity.getName());
        Assertions.assertEquals(expectedDescription, entity.getDescription());
        Assertions.assertEquals(expectedIsActive, entity.isActive());

        Assertions.assertEquals(category.getCreatedAt(), entity.getCreatedAt());

        Assertions.assertTrue(category.getUpdatedAt().isBefore(entity.getUpdatedAt()));
        Assertions.assertEquals(result.getUpdatedAt(), entity.getUpdatedAt());

        Assertions.assertNotEquals(category.getDeletedAt(), entity.getDeletedAt());
        Assertions.assertEquals(result.getDeletedAt(), entity.getDeletedAt());
    }

    // Delete
    @Test
    public void givenAPersistedCategory_whenCallsDeleteByID_shouldReturnTheDeletedCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertEquals(0, repository.count());

        this.gateway.create(category);

        Assertions.assertEquals(1, repository.count());

        final var result = this.gateway.deleteById(category.getId());

        // Assert

        // Check if the deleted entity.
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getId(), result.getId());
        Assertions.assertEquals(category.getName(), result.getName());
        Assertions.assertEquals(category.getDescription(), result.getDescription());
        Assertions.assertEquals(category.isActive(), result.isActive());

        Assertions.assertEquals(category.getCreatedAt(), result.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), result.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), result.getDeletedAt());

        // Check if the entity was deleted from the database
        Assertions.assertEquals(0, this.repository.count());
    }


    // Delete
    @Test
    public void givenAnInvalidCategoryID_whenCallsDeleteByID_shouldThrowAnError() {
        final var invalidCategoryID = CategoryID.from("Invalid_id");

        Assertions.assertEquals(0, this.repository.count());

        // Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> this.gateway.deleteById(invalidCategoryID));

        Assertions.assertEquals(0, this.repository.count());
    }

    // FindById
    @Test
    public void givenAPersistentCategory_whenCallsFindById_shouldReturnTheCategory() {
        // Arrange
        final var expectedName = "Category";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var category = Category.create(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertEquals(0, repository.count());

        this.gateway.create(category);

        Assertions.assertEquals(1, repository.count());

        final var result = this.gateway.findById(category.getId()).get();

        // Assert
        Assertions.assertNotNull(result);

        Assertions.assertEquals(category.getId(), result.getId());
        Assertions.assertEquals(category.getName(), result.getName());
        Assertions.assertEquals(category.getDescription(), result.getDescription());
        Assertions.assertEquals(category.isActive(), result.isActive());

        Assertions.assertEquals(category.getCreatedAt(), result.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), result.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), result.getDeletedAt());
    }

    // FindById
    @Test
    public void givenAnInvalidID_whenCallsFindById_shouldReturnEmptySet() {
        // Arrange
        final var invalidCategoryID = CategoryID.from("Invalid_id");

        final var result = this.gateway.findById(invalidCategoryID);

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    // FindAll
    @Test
    public void givenAPersistentCategory_whenCallsFindAll_shouldReturnAllCategories() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 7;
        final var expectedItemsCount = 3;

        final var expectedFirstItemName = "Animes";

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(0, 3, "", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());

        // First item
        final var firstItem = results.items().get(0);

        Assertions.assertNotNull(firstItem);
        Assertions.assertEquals(expectedFirstItemName, firstItem.getName());
    }

    @Test
    public void givenAPersistentCategory_whenCallsFindAllFiltering_shouldReturnSomeCategories() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 7;
        final var expectedItemsCount = 3;

        final var expectedFirstItemName = "Movies";

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());
    }

    // FindAll
    @Test
    public void givenAEmptySet_whenCallsFindAll_shouldReturnAnEmptySet() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, repository.count());

        // Act
        final var query = new CategorySearchQuery(0, 3, "", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.items().size());
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
    }

    // FindAll
    @Test
    public void givenAPageGreaterThanTheTotal_whenCallsFindAll_shouldReturnAnEmptySet() {
        // Arrange
        final var expectedPage = 3;
        final var expectedPerPage = 3;
        final var expectedTotal = 7;
        final var expectedItemsCount = 0;

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(3, 3, "", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());
    }

    // FindAll
    @Test
    public void givenATermToSearch_whenCallsFindAll_shouldReturnTheCorrectItems() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 1;
        final var expectedItemsCount = 1;

        final var expectedFirstItemName = "Documentary";

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(0, 3, "Doc", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());

        // First item
        final var firstItem = results.items().get(0);

        Assertions.assertNotNull(firstItem);
        Assertions.assertEquals(expectedFirstItemName, firstItem.getName());
    }

    // FindAll
    @Test
    public void givenAnInvalidTermToSearch_whenCallsFindAll_shouldReturnAnEmptySet() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 0;
        final var expectedItemsCount = 0;

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(0, 3, "Invalid", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());
    }

    // FindAll
    @Test
    public void givenAQueryToSortByName_whenCallsFindAll_shouldReturnTheCorrectOrder() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 7;
        final var expectedItemsCount = 3;

        final var expectedFirstItemName = "Animes";

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(0, 3, "", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());

        // First item
        final var firstItem = results.items().get(0);

        Assertions.assertNotNull(firstItem);
        Assertions.assertEquals(expectedFirstItemName, firstItem.getName());
    }

    // FindAll
    @Test
    public void givenAQueryToSortByDescription_whenCallsFindAll_shouldReturnTheCorrectOrder() {
        // Arrange
        final var expectedPage = 1;
        final var expectedPerPage = 3;
        final var expectedTotal = 7;
        final var expectedItemsCount = 3;

        final var expectedFirstItemName = "Most Watched";

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(1, 3, "", "description", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());

        // First item
        final var firstItem = results.items().get(0);

        Assertions.assertNotNull(firstItem);
        Assertions.assertEquals(expectedFirstItemName, firstItem.getName());
    }

    // FindAll
    @Test
    public void givenADifferentPage_whenCallsFindAll_shouldReturnTheCorrectPage() {
        // Arrange
        final var expectedPage = 1;
        final var expectedPerPage = 3;
        final var expectedTotal = 7;
        final var expectedItemsCount = 3;

        final var expectedFirstItemName = "Most Watched";

        Assertions.assertEquals(0, repository.count());

        this.createCategories();

        Assertions.assertEquals(7, repository.count());

        // Act
        final var query = new CategorySearchQuery(1, 3, "", "name", "asc");
        final var results = this.gateway.findAll(query);

        // Assert
        Assertions.assertNotNull(results);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());

        // First item
        final var firstItem = results.items().get(0);

        Assertions.assertNotNull(firstItem);
        Assertions.assertEquals(expectedFirstItemName, firstItem.getName());
    }

    private void createCategories() {
        final var categories = List.of(
                Category.create("Movies", "Movies Category", true),
                Category.create("Series", "Series Category", false),
                Category.create("Documentary", "Documentary Category", true),
                Category.create("Animes", "Anime Category ", true),
                Category.create("Cartoon", "Cartoons Category", false),
                Category.create("Most Watched", "Most Watched Category", true),
                Category.create("Top 10", "Top 10 Category", true)
        );

        this.repository.saveAll(categories
                .stream()
                .map(CategoryJPAEntity::from)
                .toList()
        );
    }
}
package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.configuration.useCases;

import org.matheusjfa.codeflix.administrator.catalogue.application.category.create.CreateCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.create.DefaultCreateCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.delete.DefaultDeleteCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.delete.DeleteCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.get.GetCategoryByIdUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list.DefaultListCategoriesUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.retrieve.list.ListCategoriesUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.update.DefaultUpdateCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.application.category.update.UpdateCategoryUseCase;
import org.matheusjfa.codeflix.administrator.catalogue.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfiguration {

    private final CategoryGateway gateway;

    public CategoryUseCaseConfiguration(final CategoryGateway gateway) {
        this.gateway = gateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(gateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(gateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(gateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoryUseCase() {
        return new DefaultListCategoriesUseCase(gateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryUseCase() {
        return new DefaultGetCategoryByIdUseCase(gateway);
    }
}

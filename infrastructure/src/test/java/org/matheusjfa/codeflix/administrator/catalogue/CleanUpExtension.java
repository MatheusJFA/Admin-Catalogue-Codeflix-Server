package org.matheusjfa.codeflix.administrator.catalogue;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class CleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        final var repositories = SpringExtension.getApplicationContext(extensionContext)
                .getBeansOfType(CrudRepository.class)
                .values();

        cleanUpRepositories(repositories);
    }

    private void cleanUpRepositories(Iterable<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}

package org.matheusjfa.codeflix.administrator.catalogue.domain.category;

import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.ValidationHandler;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private static final int MAX_NAME_LENGTH = 255;
    private static final int MIN_NAME_LENGTH = 3;

    private final Category category;

    public CategoryValidator(Category category, ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        final var handler = this.getHandler();
        if (name == null) {
            handler.append(new Error("'name' should not be null"));
        } else if (name.isBlank()) {
            handler.append(new Error("'name' should not be blank"));
        } else {
            final var length = name.trim().length();

            if (length < MIN_NAME_LENGTH) {
                handler.append(new Error("'name' should have at least " + MIN_NAME_LENGTH + " characters"));
            } else if (length > MAX_NAME_LENGTH) {
                handler.append(new Error("'name' should have at most " + MAX_NAME_LENGTH + " characters"));
            }
        }
    }
}

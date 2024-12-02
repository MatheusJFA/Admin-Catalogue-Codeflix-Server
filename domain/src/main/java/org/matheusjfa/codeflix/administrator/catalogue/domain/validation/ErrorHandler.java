package org.matheusjfa.codeflix.administrator.catalogue.domain.validation;

import java.util.List;

public interface ErrorHandler {
    List<Error> getErrors();
    default boolean hasErrors() {
        return !getErrors().isEmpty();
    }
}

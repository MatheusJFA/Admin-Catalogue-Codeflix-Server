package org.matheusjfa.codeflix.administrator.catalogue.domain.validation;

import java.util.List;

public interface ValidationHandler extends ErrorHandler {
    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);

}



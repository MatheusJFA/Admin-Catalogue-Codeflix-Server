package org.matheusjfa.codeflix.administrator.catalogue.domain.validation.handler;

import org.matheusjfa.codeflix.administrator.catalogue.domain.exceptions.DomainException;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Error;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.Validation;
import org.matheusjfa.codeflix.administrator.catalogue.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {
    private final List<Error> errors;

    public Notification(List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable throwable) {
        final var error = new Error(throwable.getMessage());
        return create(error);
    }

    public static Notification create(final Error error) {
        final var errors = new ArrayList<Error>();
        errors.add(error);
        return new Notification(errors);
    }


    @Override
    public ValidationHandler append(final Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public ValidationHandler validate(final Validation validation) {
        try {
            validation.validate();

        } catch (final DomainException exception) {
            this.errors.addAll(exception.getErrors());
        } catch (final Throwable t) {
            return create(t);
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}

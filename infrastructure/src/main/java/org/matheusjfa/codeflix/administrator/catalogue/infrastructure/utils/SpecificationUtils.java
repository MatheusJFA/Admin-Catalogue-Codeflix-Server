package org.matheusjfa.codeflix.administrator.catalogue.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {
    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(String attribute, String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(attribute)), like(value.toUpperCase()));
    }

    private static String like(String value) {
        return "%" + value + "%";
    }
}

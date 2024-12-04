package org.matheusjfa.codeflix.administrator.catalogue.infrastructure;

import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.configuration.WebserverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebserverConfiguration.class, args);
    }
}
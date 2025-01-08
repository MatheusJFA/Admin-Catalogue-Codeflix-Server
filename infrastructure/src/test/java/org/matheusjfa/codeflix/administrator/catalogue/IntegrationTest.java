package org.matheusjfa.codeflix.administrator.catalogue;

import org.junit.jupiter.api.extension.ExtendWith;
import org.matheusjfa.codeflix.administrator.catalogue.infrastructure.configuration.WebserverConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = WebserverConfiguration.class)
@ExtendWith(CleanUpExtension.class)
public @interface IntegrationTest {
}


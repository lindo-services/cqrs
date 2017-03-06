package services.lindo.cqrs.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vanbauwe on 17/01/2017.
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    ParameterNamesModule getParameterNamesModule() {
        return new ParameterNamesModule(JsonCreator.Mode.PROPERTIES);
    }

    @Bean
    Jdk8Module getJdk8Module() {
        return new Jdk8Module();
    }

    @Bean
    JavaTimeModule getJavaTimeModule() {
        return new JavaTimeModule();
    }

}

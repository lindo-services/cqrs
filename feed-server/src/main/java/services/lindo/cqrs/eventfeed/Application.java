package services.lindo.cqrs.eventfeed;

import com.fasterxml.jackson.databind.util.TokenBuffer;
import services.lindo.cqrs.eventfeed.feed.repository.EventRepository;
import services.lindo.cqrs.eventfeed.feed.repository.EventRepositoryJdbcImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

/**
 * Created by vanbauwe on 13/01/2017.
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"eu.combo.cqrs"})
@EnableCircuitBreaker
@EnableHystrix
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    EventRepository<TokenBuffer> getTokenBufferEventRepository() {
        return new EventRepositoryJdbcImpl<>(TokenBuffer.class);
    }
}
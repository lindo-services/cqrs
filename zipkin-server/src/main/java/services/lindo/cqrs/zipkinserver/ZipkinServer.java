package services.lindo.cqrs.zipkinserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * Created by vanbauwe on 17/01/2017.
 */
@SpringBootApplication
@EnableZipkinServer
public class ZipkinServer {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinServer.class, args);
    }
}
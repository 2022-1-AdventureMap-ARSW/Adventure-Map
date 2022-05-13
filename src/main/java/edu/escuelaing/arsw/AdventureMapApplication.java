package edu.escuelaing.arsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.escuelaing.arsw"})
public class AdventureMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdventureMapApplication.class, args);
    }
}


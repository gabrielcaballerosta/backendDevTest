package com.github.gabrielcaballerosta.tt.ams.service.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableCaching
@ComponentScan({
        "com.github.gabrielcaballerosta.tt.ams"
})
public class App {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(App.class, args);
    }

}

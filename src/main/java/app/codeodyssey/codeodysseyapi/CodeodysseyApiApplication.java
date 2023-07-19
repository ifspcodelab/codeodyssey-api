package app.codeodyssey.codeodysseyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodeodysseyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeodysseyApiApplication.class, args);
    }
}

package app.codeodyssey.codeodysseyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CodeodysseyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeodysseyApiApplication.class, args);
    }
}

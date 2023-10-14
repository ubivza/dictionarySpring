package ru.aleksandr.dictionaryspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.aleksandr.dictionaryspring.view.ApplicationRunner;

@SpringBootApplication
public class DictionarySpringApplication implements CommandLineRunner {
    private ApplicationRunner applicationRunner;

    @Autowired
    public DictionarySpringApplication(ApplicationRunner applicationRunner) {
        this.applicationRunner = applicationRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(DictionarySpringApplication.class, args);
    }

    @Override
    public void run(String... args) {
        applicationRunner.runApp();
    }
}

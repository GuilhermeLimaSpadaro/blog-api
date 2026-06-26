package com.gspadaro.blogapi.config;

import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile(value = "test")
public class Instantiation implements CommandLineRunner {
    private UserRepository userRepo;

    public Instantiation(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        userRepo.deleteAll();

        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        userRepo.saveAll(List.of(maria, alex, bob));
    }
}

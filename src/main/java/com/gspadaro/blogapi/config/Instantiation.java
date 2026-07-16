package com.gspadaro.blogapi.config;

import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.dto.AuthorDTO;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Configuration
@Profile(value = "test")
public class Instantiation implements CommandLineRunner {
    private final UserRepository userRepo;
    private final PostRepository postRepo;

    public Instantiation(UserRepository userRepo, PostRepository postRepo) {
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    @Override
    public void run(String @NonNull ... args) {
        userRepo.deleteAll();
        postRepo.deleteAll();

        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        userRepo.saveAll(List.of(maria, alex, bob));

        Post post00 = new Post(null, LocalDate.now(ZoneOffset.UTC), "Burguer king meu fast food favorito", "Adoro burguer king. Abraços!", AuthorDTO.from(maria));
        Post post01 = new Post(null, LocalDate.of(2018, 3, 21), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", AuthorDTO.from(alex));
        Post post02 = new Post(null, LocalDate.of(2018, 3, 23), "Bom dia", "Acordei feliz hoje!", AuthorDTO.from(bob));

        postRepo.saveAll(List.of(post00, post01, post02));
    }
}

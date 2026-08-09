package com.gspadaro.blogapi.config;

import com.gspadaro.blogapi.domain.Comment;
import com.gspadaro.blogapi.domain.Post;
import com.gspadaro.blogapi.domain.User;
import com.gspadaro.blogapi.repository.CommentRepository;
import com.gspadaro.blogapi.repository.PostRepository;
import com.gspadaro.blogapi.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Configuration
@Profile(value = "test")
public class Instantiation implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Instantiation(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override

    public void run(String @NonNull ... args) {
        userRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();

        User maria = new User(null, "Maria Brown", "maria@gmail.com", "1111", "11111");
        User alex = new User(null, "Alex Green", "alex@gmail.com", "22222", "33333");
        User bob = new User(null, "Bob Grey", "bob@gmail.com", "22222", "11111");

        userRepository.saveAll(List.of(maria, alex, bob));

        Post post00 = new Post(null, Instant.now(), "Burguer king meu fast food favorito", "Adoro burguer king. Abraços!", alex);
        Post post01 = new Post(null, Instant.now(), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", maria);
        Post post02 = new Post(null, Instant.now(), "Bom dia", "Acordei feliz hoje!", maria);

        postRepository.saveAll(List.of(post00, post01, post02));

        Comment comment01 = new Comment("Boa viagem mano!", alex, post00);
        Comment comment02 = new Comment("Aproveite!", bob, post02);
        Comment comment03 = new Comment("Tenha um ótimo dia!", alex, post01);

        commentRepository.saveAll(List.of(comment01, comment02, comment03));
    }
}

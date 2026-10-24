package br.com.gspadaro.blogapi.dto.post;

import br.com.gspadaro.blogapi.dto.user.UserDetailsDTO;

import java.time.Instant;

public record PostResponseDTO(String id, Instant date, String title, String body, UserDetailsDTO user) {
}

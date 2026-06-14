package com.gspadaro.blogapi.dto;

import java.time.LocalDateTime;

public record CommentRequestDTO (String text, LocalDateTime date, UserRequestDTO author){
}

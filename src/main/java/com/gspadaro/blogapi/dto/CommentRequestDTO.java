package com.gspadaro.blogapi.dto;

import java.time.LocalDateTime;

public record CommentRequestDTO (String id, String text, LocalDateTime date){
}

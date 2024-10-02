package com.audition.exception;

import jakarta.validation.constraints.NotBlank;

 public class PostNotFoundException extends RuntimeException {
 private static final long serialVersionUID = 1L;
 
 public PostNotFoundException(@NotBlank(message = "Post ID cannot be blank") final String postId) {
 super("Post not found with id: " + postId);
 }
}
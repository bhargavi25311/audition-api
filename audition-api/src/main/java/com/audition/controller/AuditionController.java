package com.audition.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/posts")
 public class AuditionController {
 @Autowired
 AuditionService auditionService;
 
  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  
  public @ResponseBody List<AuditionPost> getPosts(@RequestParam(value = "title", required = true) final String title,
 @RequestParam(value = "userId", required = true)final Long userId) {
 // Fetch all posts from service
 List<AuditionPost> posts = auditionService.getPosts();
 // Apply filters based on query params (filter by title or userId if provided)
 if (title != null) {
 posts = posts.stream().filter(post -> post.getTitle().equalsIgnoreCase(title))
 .collect(Collectors.toList());
 }
if (userId != null) {
 posts = posts.stream().filter(post -> post.getUserId() == userId).collect(Collectors.toList());
}
 return posts;
}

 @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
 
 public ResponseEntity<?> getPostById(@PathVariable("id") @NotBlank(message = "Post ID cannot be blank")final String postId) {
 final AuditionPost auditionPost;
 try {
 auditionPost = auditionService.getPostById(postId);
 
 } catch (SystemException ex) {
 // Handle custom exception, e.g., post not found
 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
 }
 return ResponseEntity.ok(auditionPost);
 }

 @GetMapping("/{postId}/comments")
 
 public ResponseEntity<AuditionPost> getPostWithComments(@PathVariable("postId") final Long postId) {
 final AuditionPost postWithComments = auditionService.getPostWithComments(postId);
 return ResponseEntity.ok(postWithComments);
 }

 // GET API to return comments separately for a post
 @GetMapping("/{postId}/only-comments")
 
 public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable("postId") final Long postId) {
 final List<Comment> comments = auditionService.getCommentsForPost(postId);
 return ResponseEntity.ok(comments);
 }

}

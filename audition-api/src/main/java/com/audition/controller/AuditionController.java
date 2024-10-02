package com.audition.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.audition.exception.PostNotFoundException;
import com.audition.exception.ResourceNotFoundException;
import com.audition.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/posts")
@Tag(name = "User Management", description = "Operations related to user management")
public class AuditionController {
 @Autowired
 AuditionService auditionService;

 /**
 * Retrieves a list of all audition posts from the external API.
 *
 * @return a list of {@link AuditionPost} objects representing all posts.
 * @throws ExternalApiException if there is an error while fetching data from
 *                              the API.
 */

 @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
 @Operation(summary = "Getpostsby title and user id", description = "Retrievesa posts detailsby titleanduserid")
 public @ResponseBody List<AuditionPost> getPosts(@RequestParam(value = "title", required = true) final String title,
 @RequestParam(value = "userId", required = true) final Long userId) {
 // Fetch all posts from service
 List<AuditionPost> posts = auditionService.getPosts();
 // Apply filters based on query params (filter by title or userId if provided)
 if (title != null) {
 posts = posts.stream().filter(post -> post.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());
 }
 if (userId != null) {
 posts = posts.stream().filter(post -> post.getUserId() == userId).collect(Collectors.toList());
 }
 return posts;
 }

 /**
 * Retrieves a specific audition post by its ID.
 *
 * @param postId the ID of the audition post to retrieve.
 * @return an {@link AuditionPost} object representing the specified post.
 * @throws ResourceNotFoundException if no post is found with the given ID.
 * @throws ExternalApiException      if there is an error while fetching data
 *                                   from the API.
 */

 @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
 @Operation(summary = "Get a user by ID", description = "Retrieves a user based on the provided ID", responses = {
 @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
 @ApiResponse(responseCode = "404", description = "User not found") })
 public ResponseEntity<?> getPostById(
 @PathVariable("id") @NotBlank(message = "Post ID cannot be blank") final String postId) {
 final Optional<AuditionPost> optionalAuditionPost = auditionService.getPostById(postId);
 return ResponseEntity.ok(optionalAuditionPost);
 }

 /**
 * Retrieves comments associated with a specific audition post.
 *
 * @param postId the ID of the audition post for which to retrieve comments.
 * @return a list of {@link Comment} objects associated with the specified post.
 * @throws PostNotFoundException if no comments are found for the given post ID.
 * @throws ExternalApiException  if there is an error while fetching data from
 *                               the API.
 */

 @GetMapping("/{postId}/comments")
 @Operation(summary = "Get a comments by postid", description = "Retrieves a comments based on the provided POSTID", responses = {
 @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
 @ApiResponse(responseCode = "404", description = "User not found") })
 public ResponseEntity<?> getPostWithComments(@PathVariable("postId") final Long postId) {
 try {
 final Optional<AuditionPost> postWithComments = auditionService.getPostWithComments(postId);
 if (postWithComments.isPresent()) {
 return ResponseEntity.ok(postWithComments.get());
 } // 200 OK
 } catch (SystemException ex) {
 // Handle custom exception, e.g., post not found
 throw new PostNotFoundException(postId.toString());
 }
 return ResponseEntity.ok(postId.toString());
}

 // GET API to return comments separately for a post
 /**
 * Retrieves comments associated with a specific audition post.
 *
 * @param postId the ID of the audition post for which to retrieve
 *               only-comments.
 * @return a list of {@link Comment} objects associated with the specified post.
 * @throws PostNotFoundException if no comments are found for the given post ID.
 * @throws ExternalApiException  if there is an error while fetching data from
 *                               the API.
 */
 @GetMapping("/{postId}/only-comments")
 @Operation(summary = "Get a only-comments by postid", description = "Retrievesa only-comments based on the provided POSTID", responses = {
 @ApiResponse(responseCode = "200", description ="Successful retrieval of user"),
 @ApiResponse(responseCode = "404", description = "User not found") })
 public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable("postId") final Long postId) {
 final List<Comment> comments = auditionService.getCommentsForPost(postId);
 return ResponseEntity.ok(comments);
}
}

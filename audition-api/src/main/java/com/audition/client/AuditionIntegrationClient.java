package com.audition.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.audition.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;

@Component
public class AuditionIntegrationClient {
 private RestTemplate restTemplate = new RestTemplate();
 private static final String POST_URL = "https://jsonplaceholder.typicode.com/posts/{postId}";
 private static final String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments?postId={postId}";
 
 public List<AuditionPost> getPosts() {
 final String url = "https://jsonplaceholder.typicode.com/posts";
 // Make RestTemplate call to get posts
 final ResponseEntity<AuditionPost[]> response = restTemplate.getForEntity(url, AuditionPost[].class);
 // Convert the array response to a List
 return Arrays.asList(response.getBody());
}

 public AuditionPost getPostById(final String id) {
 // TODO get post by post ID call from
 // https://jsonplaceholder.typicode.com/posts/
 final String url = "https://jsonplaceholder.typicode.com/posts/" + id;
 try {
 // Make RestTemplate call to get post by id
 final ResponseEntity<AuditionPost> response = restTemplate.getForEntity(url, AuditionPost.class);
 return response.getBody();
 } catch (HttpClientErrorException e) {
 if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
 // If the post is not found, throw a SystemException with a proper message
 throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", e);
 } else {
final  String errorMessage = e.getResponseBodyAsString();
 throw new SystemException("Error fetching post: " + errorMessage, e); 
 }
 } catch (RestClientException e) {
 // Handle other exceptions such as connection timeouts
 throw new SystemException("An unexpected error occurred while fetching post by id " + id, e);
 }
}

 // TODO Write a method GET comments for a post from
 // https://jsonplaceholder.typicode.com/posts/{postId}/comments - the comments
 // must be returned as part of the post.
 public AuditionPost getPostWithComments(final Long postId) {
 // Fetch the post by postId
 final AuditionPost post = restTemplate.getForObject(POST_URL, AuditionPost.class, postId);
 // Fetch the comments for the given postId
 final Comment[] comments = restTemplate.getForObject(COMMENTS_URL, Comment[].class, postId);
 // Add comments to the post (if required to return as part of the post)
 if (post != null && comments != null) {
 post.setComments(Arrays.asList(comments));
 }
 return post; // Return the post with comments
 }

 public List<Comment> getCommentsForPost(final Long postId) {
 final Comment[] comments = restTemplate.getForObject(COMMENTS_URL, Comment[].class, postId);
 return comments != null ? Arrays.asList(comments) : Collections.emptyList();
 }
}

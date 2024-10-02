package com.audition.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.audition.exception.PostNotFoundException;
import com.audition.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;

@Component
public class AuditionIntegrationClient {
 @Value("${api.base-url}")
 private String baseUrl;

 @Value("${api.posts-url}")
 private String postsUrl;

 @Value("${api.comments-url}")
 private String commentsUrl;
 @Autowired
 private RestTemplate restTemplate;
 
 public List<AuditionPost> getPosts() {
 final String url = baseUrl + "/posts/";
 // Make RestTemplate call to get posts
 final ResponseEntity<AuditionPost[]> response = restTemplate.getForEntity(url, AuditionPost[].class);
 // Convert the array response to a List
 return Arrays.asList(response.getBody());
 }

 public AuditionPost getPostById(final String id) {
 final String url = baseUrl + "/posts/" + id;
 try {
 // Make RestTemplate call to get post by id
 final ResponseEntity<AuditionPost> response = restTemplate.getForEntity(url, AuditionPost.class);
 return response.getBody();
 } catch (HttpClientErrorException e) {
 if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
 // If the post is not found, throw a post id not found expection with a proper
 // message
 throw new PostNotFoundException(id);
 } else {
 final String errorMessage = e.getResponseBodyAsString();
 throw new SystemException("Error fetching post: " + errorMessage, e);
 }
 } catch (RestClientException e) {
 throw new SystemException("An unexpected error occurred while fetching post by id " + id, e);
 }
 }

 public AuditionPost getPostWithComments(final Long postId) {
 // Fetch the post by postId
 final AuditionPost post = restTemplate.getForObject(baseUrl + postsUrl, AuditionPost.class, postId);
 // Fetch the comments for the given postId
 final Comment[] comments = restTemplate.getForObject(baseUrl + commentsUrl, Comment[].class, postId);
 // Add comments to the post (if required to return as part of the post)
 if (Objects.nonNull(post) && Objects.nonNull(comments)) {
 post.setComments(Arrays.asList(comments));
 }
 return post; // Return the post with comments
 }

 public List<Comment> getCommentsForPost(final Long postId) {
 final Comment[] comments = restTemplate.getForObject(baseUrl + commentsUrl, Comment[].class, postId);
 return comments != null ? Arrays.asList(comments) : Collections.emptyList();
 }
}

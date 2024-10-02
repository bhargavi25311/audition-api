package com.audition.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.audition.client.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;

@SpringBootTest
@AutoConfigureMockRestServiceServer
 class AuditionIntegrationClientCommentTest {
 @Autowired
 private AuditionIntegrationClient auditionIntegrationClient;
 @Autowired
 private RestTemplate restTemplate;
 private MockRestServiceServer mockServer;
 private static final  String POSTS_URL = "/posts/";
 private static final  String COMMENTS_URL = "/comments?postId=";
 private static final  String BASE_URL = "https://jsonplaceholder.typicode.com";
 
 @BeforeEach
 public void setUp() {
 mockServer = MockRestServiceServer.createServer(restTemplate);
 }
 
 @Test
 void testGetPostWithCommentsSuccess() {
 // Arrange
 final Long postId = 1L;
 final String postJson = "{\"id\":1,\"title\":\"Post Title 1\",\"userId\":1}";
 final String commentsJson = "[{\"id\":101,\"postId\":1,\"name\":\"Commenter 1\",\"email\":\"commenter1@example.com\",\"body\":\"Comment body 1\"},"
 + "{\"id\":102,\"postId\":1,\"name\":\"Commenter 2\",\"email\":\"commenter2@example.com\",\"body\":\"Comment body 2\"}]";
 mockServer.expect(once(), requestTo(BASE_URL + POSTS_URL + postId))
 .andExpect(method(org.springframework.http.HttpMethod.GET))
 .andRespond(withSuccess(postJson, MediaType.APPLICATION_JSON));
 mockServer.expect(once(), requestTo(BASE_URL + COMMENTS_URL + postId))
 .andExpect(method(org.springframework.http.HttpMethod.GET))
 .andRespond(withSuccess(commentsJson, MediaType.APPLICATION_JSON));
 // Act
 final AuditionPost post = auditionIntegrationClient.getPostWithComments(postId);
 // Assert
 assertNotNull(post, "The returned post should not be null");
 final List<Comment> comments = post.getComments();
 assertNotNull(comments, "Comments should not be null");
 assertThat(comments, hasSize(2));
 final Comment comment1 = comments.get(0);
 assertEquals("Comment body 1", comment1.getBody(), "First comment's body should match");
 final Comment comment2 = comments.get(1);
 assertEquals(102L, comment2.getId(), "Second comment ID should be 102");
 assertEquals(1L, comment2.getPostId(), "Second comment's post ID should be 1");
 assertEquals("Commenter 2", comment2.getName(), "Second commenter's name should match");
 // Verify that all expectations were met
 mockServer.verify();
 }
}

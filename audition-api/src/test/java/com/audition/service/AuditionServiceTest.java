package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.audition.client.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;


 class AuditionServiceTest {
 @Mock
 private AuditionIntegrationClient auditionIntegrationClient;
 @InjectMocks
 private AuditionService auditionService;
 
 @BeforeEach
 void setUp() {
 MockitoAnnotations.openMocks(this);
 }

 @Test
 void testGetPosts() {
 // Arrange: create a mock list of AuditionPost objects
 final AuditionPost mockPost = new AuditionPost();
 mockPost.setId("1");
 mockPost.setTitle("Sample Post");
 mockPost.setUserId(1L);
 mockPost.setBody(null);

 final AuditionPost post2 = new AuditionPost();
 post2.setId("2");
 post2.setTitle("Sample Post2");
 post2.setUserId(2L);
 post2.setBody(null);
 final List<AuditionPost> mockPosts = Arrays.asList(mockPost, post2);
 // Stub: define the behavior of the mocked client
 when(auditionIntegrationClient.getPosts()).thenReturn(mockPosts);
 // Act: call the method under test
 final List<AuditionPost> result = auditionService.getPosts();
 // Assert: verify the behavior and results
 assertEquals(2, result.size());
 assertEquals("Sample Post", result.get(0).getTitle());
 assertEquals("Sample Post2", result.get(1).getTitle());
 // Verify that the auditionIntegrationClient.getPosts() was called once
 verify(auditionIntegrationClient, times(1)).getPosts();
 }

 @Test
 void testGetPostByIdSuccess() {
 // Arrange: create a mock AuditionPost object
 final String postId = "1";
 final AuditionPost mockPost = new AuditionPost();
 mockPost.setId("1");
 mockPost.setTitle("Sample Post for sucess");
 mockPost.setUserId(1L);
 mockPost.setBody(null);
 // Stub: define the behavior of the mocked client
 when(auditionIntegrationClient.getPostById(postId)).thenReturn(mockPost);
 // Act: call the method under test
 final AuditionPost result = auditionService.getPostById(postId);
 // Assert: verify the behavior and results
 assertEquals("Sample Post for sucess", result.getTitle());
 assertEquals("1", result.getId());
 // Verify that the auditionIntegrationClient.getPostById() was called once
 verify(auditionIntegrationClient, times(1)).getPostById(postId);
 }

 @Test
 void testGetPostWithCommentsSuccess() {
 // Arrange: create a mock AuditionPost object with comments
 final Long postId = 1L;
 final AuditionPost postWithComments = createSampleAuditionPost(postId);
 // Stub: define the behavior of the mocked client
 when(auditionIntegrationClient.getPostWithComments(postId)).thenReturn(postWithComments);
 // Act: call the method under test
 final AuditionPost result = auditionService.getPostWithComments(postId);
 // Assert: verify the behavior and results
 assertEquals("Audition for Lead Role", result.getTitle());
 assertEquals(2, result.getComments().size());
 assertEquals("This is a description of the audition post test.", result.getComments().get(0).getBody());
 assertEquals("This is a description of the audition post.", result.getComments().get(1).getBody());
 // Verify that the auditionIntegrationClient.getPostWithComments() was called
 verify(auditionIntegrationClient, times(1)).getPostWithComments(postId);
 }

 AuditionPost createSampleAuditionPost(final Long postId) {
 final Comment comment1 = new Comment();
 comment1.setId(2L);
 comment1.setBody("This is a description of the audition post test.");
 comment1.setEmail("email");
 comment1.setPostId(2L);
 comment1.setName("John Doe");

 final Comment comment2 = new Comment();
 comment2.setId(2L);
 comment2.setBody("This is a description of the audition post.");
 comment2.setEmail("email1");
 comment2.setPostId(3L);
 comment2.setName("John Doe1");
 // Create AuditionPost
 final AuditionPost post = new AuditionPost();
 post.setUserId(100L); // Example userId
 post.setTitle("Audition for Lead Role");
 post.setBody("This is a description of the audition post.");
 post.setComments(Arrays.asList(comment1, comment2));
 return post;
 }

 @Test
 void testGetCommentsForPostSuccess() {
 // Arrange: create a postId and mock Comment objects
 final Long postId = 1L;
 final Comment comment1 = new Comment();
 comment1.setId(2L);
 comment1.setBody("This is a description of the audition post test for sucess.");
 comment1.setEmail("email");
 comment1.setPostId(2L);
 comment1.setName("John Doe");

 final Comment comment2 = new Comment();
 comment2.setId(2L);
 comment2.setBody("This is a description of the audition post for sucess.");
 comment2.setEmail("email1");
 comment2.setPostId(3L);
 comment2.setName("John Doe1");
 final List<Comment> mockComments = Arrays.asList(comment1, comment2);
 // Stub: define the behavior of the mocked client
 when(auditionIntegrationClient.getCommentsForPost(postId)).thenReturn(mockComments);
 // Act: call the method under test
 final List<Comment> result = auditionService.getCommentsForPost(postId);
 // Assert: verify the behavior and results
 // assertNotNull(result);
 assertEquals(2, result.size());
 assertEquals("This is a description of the audition post test for sucess.", result.get(0).getBody());
 assertEquals("This is a description of the audition post for sucess.", result.get(1).getBody());
 // Verify that the auditionIntegrationClient.getCommentsForPost() was called
 verify(auditionIntegrationClient, times(1)).getCommentsForPost(postId);
 }
}

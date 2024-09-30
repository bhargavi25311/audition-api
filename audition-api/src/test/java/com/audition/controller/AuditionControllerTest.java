package com.audition.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.audition.config.TestSecurityConfig;
import com.audition.exception.ResourceNotFoundException;
import com.audition.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;

@WebMvcTest(AuditionController.class)
@Import(TestSecurityConfig.class)
class AuditionControllerTest {
 @Autowired
 private MockMvc mockMvc;
 @MockBean
 private AuditionService auditionService;
 @MockBean
 private AuditionLogger auditionLogger;
 
 @Test
  void testGetPostWithComments() throws Exception {
 final Long postId = 9L;
 final AuditionPost postWithComments = createSampleAuditionPost(postId);
 // Mock the service layer
 when(auditionService.getPostWithComments(postId)).thenReturn(postWithComments);
 // Perform GET request and assert responses
 mockMvc.perform(get("/posts/{postId}/comments", postId).accept(MediaType.APPLICATION_JSON))
 .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Audition for Lead Role"));
 verify(auditionService, times(1)).getPostWithComments(postId);
 }

 @Test
  void testGetPostWithCommentsNotFound() throws Exception {
 final Long postId = 10L;
 when(auditionService.getPostWithComments(postId)).thenThrow(new ResourceNotFoundException("Post not found"));
 mockMvc.perform(get("/posts/{postId}/comments", postId)).andExpect(status().isNotFound());
 verify(auditionService, times(1)).getPostWithComments(postId);
 }

  AuditionPost createSampleAuditionPost(final Long postId) {
 // Create Comments
 final Comment comment1 = new Comment();
 comment1.setId(2L);
 comment1.setBody("test");
 comment1.setEmail("email");
 comment1.setPostId(2L);
 comment1.setName("John Doe");

 final Comment comment2 = new Comment();
 comment2.setId(2L);
 comment2.setBody("test1");
 comment2.setEmail("email1");
 comment2.setPostId(3L);
 comment2.setName("John Doe1");
 // Create AuditionPost
 final AuditionPost post = new AuditionPost();
 // post.setId(postId);
 post.setUserId(100L); // Example userId
 post.setTitle("Audition for Lead Role");
 post.setBody("This is a description of the audition post.");
 post.setComments(Arrays.asList(comment1, comment2));
 return post;
 }

 @Test
  void testGetCommentsForPostSuccess() throws Exception {
 final Long postId = 1L;
 final Comment comment1 = new Comment();
 comment1.setId(1L);
 comment1.setBody("test");
 comment1.setEmail("email");
 comment1.setPostId(1L);
 comment1.setName("John Doe");

final Comment comment2 = new Comment();
 comment2.setId(1L);
 comment2.setBody("test1");
 comment2.setEmail("email1");
 comment2.setPostId(1L);
 comment2.setName("John Doe1");
final List<Comment> mockComments = Arrays.asList(comment1, comment2);
 when(auditionService.getCommentsForPost(postId)).thenReturn(mockComments);
 mockMvc.perform(get("/posts/{postId}/only-comments", postId)).andExpect(status().isOk())
 .andExpect(jsonPath("$[0].name").value("John Doe"));
 verify(auditionService, times(1)).getCommentsForPost(postId);
 }

 @Test
  void testGetCommentsForPostNotFound() throws Exception {
 final Long postId = 2L;
 when(auditionService.getCommentsForPost(postId)).thenThrow(new ResourceNotFoundException("Post not found"));
 mockMvc.perform(get("/posts/{postId}/only-comments", postId)).andExpect(status().isNotFound());
 verify(auditionService, times(1)).getCommentsForPost(postId);
 }


}

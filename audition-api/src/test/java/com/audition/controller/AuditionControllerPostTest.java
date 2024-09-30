package com.audition.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.audition.common.exception.SystemException;
import com.audition.config.TestSecurityConfig;
import com.audition.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;

// Assuming AuditionController handles posts
@WebMvcTest(AuditionController.class)
@Import(TestSecurityConfig.class)
class AuditionControllerPostTest {
private static final String POST_BY_ID_PATH = "/posts/{id}";
 @Autowired
 private MockMvc mockMvc;

 @MockBean
 private AuditionService auditionService;

 @MockBean
 private AuditionLogger auditionLogger;

 @Test
 @DisplayName("GET postId  - Success")
 
 void testGetPostByIdSuccess() throws Exception {
 final String postId = "123";
 final AuditionPost mockPost = new AuditionPost();
 mockPost.setId(postId);
 mockPost.setTitle("Sample Post");
 mockPost.setUserId(1L);
 mockPost.setBody(null);
 when(auditionService.getPostById(postId)).thenReturn(mockPost);
 final ResultActions result = mockMvc.perform(get(POST_BY_ID_PATH, postId).accept(MediaType.APPLICATION_JSON));
 result.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(postId))
 .andExpect(jsonPath("$.title").value("Sample Post"));
 verify(auditionService, times(1)).getPostById(postId);
 }

 @Test
 @DisplayName("GET postId - Blank Post ID (Validation Error)")
 void testGetPostByIdBlankId() throws Exception {
 final String blankPostId = "   ";
 final ResultActions result = mockMvc.perform(get(POST_BY_ID_PATH, blankPostId).accept(MediaType.APPLICATION_JSON));
 result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
 // Verify that the service is never called due to validation failure
 verify(auditionService, never()).getPostById(anyString());
 }

 @Test
 @DisplayName("GET postId - Post Not Found (SystemException)")
 void testGetPostByIdNotFound() throws Exception {
 final String postId = "nonexistent";
 final String errorMessage = "Post not found";
 // getPostById is called with postId
 when(auditionService.getPostById(postId)).thenThrow(new SystemException(errorMessage));
 final ResultActions result = mockMvc.perform(get(POST_BY_ID_PATH, postId).accept(MediaType.APPLICATION_JSON));
 // Assert
 result.andExpect(status().isNotFound());
 // Verify that the service method was called exactly once with the specified
 // postId
 verify(auditionService, times(1)).getPostById(postId);
 }

 @Test
 @DisplayName("GET postId - Internal Server Error (Exception)")
 void testGetPostByIdInternalServerError() throws Exception {
 // Arrange
 final String postId = "123";
 // Configure the mocked AuditionService to throw a generic Exception when
 // getPostById is called with postId
 when(auditionService.getPostById(postId)).thenThrow(new RuntimeException("Database connection failed"));
 final ResultActions result = mockMvc.perform(get(POST_BY_ID_PATH, postId).accept(MediaType.APPLICATION_JSON));
 // Assert
 result.andExpect(status().isInternalServerError());
 verify(auditionService, times(1)).getPostById(postId);
 }

 @Test
 @DisplayName("GET /posts - Missing Post ID (HTTP 404)")
 void testGetPostByIdMissingId() throws Exception {
 final ResultActions result = mockMvc.perform(get("/posts") // Missing {id}
 .accept(MediaType.APPLICATION_JSON));
 // Assert
 result.andExpect(status().isBadRequest()); // 404 Not Found
 // Verify that the service is never called
 verify(auditionService, never()).getPostById(anyString());
 }

 @Test
 @DisplayName("GET /posts - No posts found for the given filters")
 @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
 void testGetPostsWithNoMatchingPosts() throws Exception {
 // Arrange
 when(auditionService.getPosts()).thenReturn(Collections.emptyList());
 // Act
 final ResultActions result = mockMvc.perform(get("/posts").param("title", "Nonexistent Title").param("userId", "999")
 .accept(MediaType.APPLICATION_JSON));
 // Assert
 result.andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0)); // No posts found
 }
}
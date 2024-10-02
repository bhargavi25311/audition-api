package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.audition.client.AuditionIntegrationClient;
import com.audition.exception.PostNotFoundException;
import com.audition.exception.SystemException;
import com.audition.model.AuditionPost;


@SpringBootTest
@AutoConfigureMockRestServiceServer
class AuditionIntegrationClientPostByIdTest {
 @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    // Define the base URL as per your configuration
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts/";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetPostByIdSuccess() {
        // Arrange: Define the expected post and mock JSON response
        final String postId = "1";
        final String jsonResponse = "{\"id\":1,\"title\":\"Post Title 1\",\"userId\":1}";

        mockServer.expect(once(), requestTo(BASE_URL + postId))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act: Call the method under test
        final AuditionPost post = auditionIntegrationClient.getPostById(postId);

        // Assert: Verify the returned post
        assertNotNull(post, "The returned post should not be null");
        assertEquals("Post Title 1", post.getTitle(), "Post title should match");
        assertEquals(1L, post.getUserId(), "User ID should be 1");

        // Verify that all expectations were met
        mockServer.verify();
    }

    @Test
    void testGetPostByIdNotFound() {
        // Arrange: Mock a 404 Not Found response
       final String postId = "999";
        mockServer.expect(once(), requestTo(BASE_URL + postId))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // Act & Assert: Expect PostNotFoundException to be thrown
        final PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            auditionIntegrationClient.getPostById(postId);
        }, "Expected getPostById to throw PostNotFoundException");

        assertEquals("Post not found with id: 999", exception.getMessage(), "Exception message should match");

        // Verify that all expectations were met
        mockServer.verify();
    }

    @Test
    void testGetPostByIdServerError() {
        // Arrange: Mock a 500 Internal Server Error response
        final String postId = "a";
        mockServer.expect(once(), requestTo(BASE_URL + postId))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withServerError());

        // Act & Assert: Expect SystemException to be thrown
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(postId);
        }, "Expected getPostById to throw SystemException");

        //assertTrue(exception.getMessage().contains("Error fetching post"), "Exception message should contain error info");
        assertTrue(exception.getTitle().contains("API Error Occurred"), "Exception message should contain error info");
        // Verify that all expectations were met
        mockServer.verify();
    }

    @Test
    void testGetPostByIdMalformedResponse() {
        // Arrange: Mock a malformed JSON response
        final String postId = "1";
        final String malformedJson = "{\"id\":1,\"title\":\"Post Title 1\",\"userId\":}"; // Malformed JSON

        mockServer.expect(once(), requestTo(BASE_URL + postId))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(malformedJson, MediaType.APPLICATION_JSON));

        // Act & Assert: Expect SystemException due to JSON parsing error
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(postId);
        }, "Expected getPostById to throw SystemException due to malformed JSON");

        assertTrue(exception.getTitle().contains("API Error Occurred"), "Exception message should contain error info");

        // Verify that all expectations were met
        mockServer.verify();
    }

    @Test
    void testGetPostByIdRestClientException() {
        // Arrange: Mock a connection timeout or other RestClientException
        final String postId = "1";
        mockServer.expect(once(), requestTo(BASE_URL + postId))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(request -> {
                    throw new org.springframework.web.client.ResourceAccessException("I/O error on GET request");
                });

        // Act & Assert: Expect SystemException due to RestClientException
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(postId);
        }, "Expected getPostById to throw SystemException due to RestClientException");

        assertTrue(exception.getMessage().contains("An unexpected error occurred"), "Exception message should indicate unexpected error");

        // Verify that all expectations were met
        mockServer.verify();
    }
}



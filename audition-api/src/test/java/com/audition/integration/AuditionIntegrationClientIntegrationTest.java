package com.audition.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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

@SpringBootTest
@AutoConfigureMockRestServiceServer
class AuditionIntegrationClientIntegrationTest {

    @Autowired
    AuditionIntegrationClient auditionIntegrationClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts/";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
     void testGetPostsSuccess() {
        // Arrange
        final String jsonResponse = "[{\"id\":1,\"title\":\"Post Title 1\",\"userId\":1}," + "{\"id\":2,\"title\":\"Post Title 2\",\"userId\":2}]";

        mockServer.expect(once(), requestTo(BASE_URL))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        final List<AuditionPost> posts = auditionIntegrationClient.getPosts();

        // Assert
        assertThat(posts, is(notNullValue()));
        assertThat(posts.size(), is(2));
        assertThat(posts.get(0).getTitle(), is("Post Title 1"));
        assertThat(posts.get(1).getTitle(), is("Post Title 2"));

        // Verify all expectations met
        mockServer.verify();
    }

    @Test
     void testGetPostsEmptyResponse() {
        // Arrange
       final String jsonResponse = "[]";

        mockServer.expect(once(), requestTo(BASE_URL))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Act
        final List<AuditionPost> posts = auditionIntegrationClient.getPosts();

        // Assert
        assertThat(posts, is(notNullValue()));
        assertThat(posts, is(empty()));
        mockServer.verify();
    }
    
    
}

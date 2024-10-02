package com.audition.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audition.client.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;

@Service
public class AuditionService {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;


    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    public Optional<AuditionPost> getPostById(final String postId) {
        return Optional.ofNullable(auditionIntegrationClient.getPostById(postId));
    }
    
    public Optional<AuditionPost> getPostWithComments(final Long postId) {
        return Optional.ofNullable(auditionIntegrationClient.getPostWithComments(postId));
    }
    
    
    public List<Comment> getCommentsForPost(final Long postId) {
        return auditionIntegrationClient.getCommentsForPost(postId);
    }

}

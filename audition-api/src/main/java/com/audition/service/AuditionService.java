package com.audition.service;

import java.util.List;

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

    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }
    
    public AuditionPost getPostWithComments(final Long postId) {
        return auditionIntegrationClient.getPostWithComments(postId);
    }
    
    
    public List<Comment> getCommentsForPost(final Long postId) {
        return auditionIntegrationClient.getCommentsForPost(postId);
    }

}

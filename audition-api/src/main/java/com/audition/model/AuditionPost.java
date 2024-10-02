package com.audition.model;

import java.util.List;

@SuppressWarnings("all")
public class AuditionPost {

 private Long userId;
 private String id;
 private String title;
 private String body;
 private List<Comment> comments;


public Long getUserId() {
 return userId;
 }

 public void setUserId(final Long userId) {
 this.userId = userId;
 }

 public String getId() {
 return id;
 }

 public void setId(final String postId) {
 this.id = postId;
 }

 public String getTitle() {
 return title;
 }

 public void setTitle(final String title) {
 this.title = title;
 }

 public String getBody() {
 return body;
 }

 public void setBody(final String body) {
 this.body = body;
 }

 public List<Comment> getComments() {
 return comments;
 }

 public void setComments(final List<Comment> comments) {
 this.comments = comments;
 }

@Override
public String toString() {
return "AuditionPost [userId=" + userId + ", id=" + id + ", title=" + title + ", body=" + body + ", comments="
 + comments + "]";
}

 

}
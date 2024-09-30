package com.audition.model;

@SuppressWarnings("all")
public class Comment {

 private Long id;
 private Long postId;
 private String name;
 private String email;
 private String body;

 public Long getId() {
 return id;
 }

 public void setId(final Long id) {
 this.id = id;
 }

 public Long getPostId() {
 return postId;
 }

 public void setPostId(final Long postId) {
 this.postId = postId;
 }

 public String getName() {
 return name;
 }

 public void setName(final String name) {
 this.name = name;
 }

 public String getEmail() {
 return email;
 }

 public void setEmail(final String email) {
 this.email = email;
 }

 public String getBody() {
 return body;
 }
 

 public void setBody(final String body) {
 this.body = body;
 }
}

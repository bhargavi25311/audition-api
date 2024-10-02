package com.audition.exception;

public class ResourceNotFoundException extends RuntimeException {

 private static final long serialVersionUID = 1L;

 private final String resourceName;
 private final Object resourceId;

 public ResourceNotFoundException(final String resourceName, final Object resourceId) {
 super(String.format("%s not found with id: %s", resourceName, resourceId));
 this.resourceName = resourceName;
 this.resourceId = resourceId;
 }

 public final String getResourceName() {
 return resourceName;
 }

 public final Object getResourceId() {
 return resourceId;
 }
}

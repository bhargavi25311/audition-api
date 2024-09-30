package com.audition.exception;

@SuppressWarnings("all")
public class SystemException extends RuntimeException {

 private static final long serialVersionUID = -5876728854007114881L;

 public static final String DEFAULT_TITLE = "API Error Occurred";
 private Integer statusCode;
 private String title;
 private String detail;

 public Integer getStatusCode() {
 return statusCode;
}

 public void setStatusCode(final Integer statusCode) {
 this.statusCode = statusCode;
 }

 public String getTitle() {
 return title;
 }

 public void setTitle(final String title) {
 this.title = title;
 }

 public String getDetail() {
 return detail;
 }

 public void setDetail(final String detail) {
 this.detail = detail;
 }

 public SystemException() {
 super();
}

 public SystemException(final String message) {
 super(message);
 this.title = DEFAULT_TITLE;
 }

 public SystemException(final String message, final Integer errorCode) {
 super(message);
 this.title = DEFAULT_TITLE;
 this.statusCode = errorCode;
 }

 public SystemException(final String message, final Throwable exception) {
 super(message, exception);
 this.title = DEFAULT_TITLE;
 }

 public SystemException(final String detail, final String title, final Integer errorCode) {
 super(detail);
 this.statusCode = errorCode;
 this.title = title;
 this.detail = detail;
 }

 public SystemException(final String detail, final String title, final Throwable exception) {
 super(detail, exception);
 this.title = title;
 this.statusCode = 500;
 this.detail = detail;
 }

 public SystemException(final String detail, final Integer errorCode, final Throwable exception) {
 super(detail, exception);
 this.statusCode = errorCode;
 this.title = DEFAULT_TITLE;
 this.detail = detail;
 }

 public SystemException(final String detail, final String title, final Integer errorCode,
 final Throwable exception) {
 super(detail, exception);
 this.statusCode = errorCode;
 this.title = title;
 this.detail = detail;
 }

@Override
public String toString() {
return "SystemException [statusCode=" + statusCode + ", title=" + title + ", detail=" + detail + "]";
}
 


}

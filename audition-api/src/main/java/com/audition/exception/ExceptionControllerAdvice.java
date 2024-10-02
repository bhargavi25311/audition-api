package com.audition.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.audition.logging.AuditionLogger;

import io.micrometer.common.util.StringUtils;


@ControllerAdvice

public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    public static final String DEFAULT_TITLE = "API Error Occurred";
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
   

    private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";

    @Autowired
  private AuditionLogger logger;

    @ExceptionHandler(HttpClientErrorException.class)
    ProblemDetail handleHttpClientException(final HttpClientErrorException e) {
        return createProblemDetail(e, e.getStatusCode());
        

    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ProblemDetail handleResourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://example.com/problems/resource-not-found"));
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "RESOURCE_NOT_FOUND");
        return problemDetail;
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ProblemDetail handleMainException(final Exception e, final WebRequest request) {
    // Log the exception details
    LOG.error("An unexpected error occurred: ", e);
   final HttpStatusCode status = getHttpStatusCodeFromException(e);
    return createProblemDetail(e, status);
    }

    @ExceptionHandler(SystemException.class)
    final ProblemDetail handleSystemException(final SystemException e, final WebRequest request) {  
    // Determine the appropriate HTTP status code
    final HttpStatusCode status = getHttpStatusCodeFromSystemException(e);
    // Create and return the ProblemDetail object
    return createProblemDetail(e, status);
    }


    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }
    
    
    // Handle Post Not Found
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePostNotFoundException(final PostNotFoundException ex) {
        final Map<String, Object> body = new ConcurrentHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Handle Invalid Post Data
    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPostException(final InvalidPostException ex) {
      final  Map<String, Object> body = new ConcurrentHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
   


}



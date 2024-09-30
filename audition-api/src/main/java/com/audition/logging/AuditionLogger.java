package com.audition.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class AuditionLogger {

    public void info(final Logger logger, final String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void info(final Logger logger, final String message, final Object object) {
        if (logger.isInfoEnabled()) {
            logger.info(message, object);
        }
    }

    public void debug(final Logger logger, final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void warn(final Logger logger, final String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void error(final Logger logger, final String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public void logErrorWithException(final Logger logger, final String message, final Exception e) {
        if (logger.isErrorEnabled()) {
            logger.error(message, e);
        }
    }

    public void logStandardProblemDetail(final Logger logger, final ProblemDetail problemDetail, final Exception e) {
        if (logger.isErrorEnabled()) {
            final var message = createStandardProblemDetailMessage(problemDetail);
            logger.error(message, e);
        }
    }

    public void logHttpStatusCodeError(final Logger logger, final String message, final Integer errorCode) {
        if (logger.isErrorEnabled()) {
            logger.error(createBasicErrorResponseMessage(errorCode, message) + "\n");
        }
    }

    private String createStandardProblemDetailMessage(final ProblemDetail standardProblemDetail) {
        // Check if the standardProblemDetail is null
        if (standardProblemDetail == null) {
            return StringUtils.EMPTY; // Return an empty string if the input is null
        }

       // Create a map to hold the details of the problem
       final ConcurrentHashMap<String, Object> problemDetailMap = new ConcurrentHashMap<>();
        problemDetailMap.put("status", standardProblemDetail.getStatus());
        problemDetailMap.put("title", standardProblemDetail.getTitle());
        problemDetailMap.put("detail", standardProblemDetail.getDetail());
        problemDetailMap.put("instance", standardProblemDetail.getInstance());
        
        // Optionally add a timestamp
        problemDetailMap.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Convert the map to JSON string (you might want to use a library like Jackson or Gson for this)
        return new Gson().toJson(problemDetailMap);
    }


    private String createBasicErrorResponseMessage(final Integer errorCode, final String message) {
    // Create a simple error response structure
    final ConcurrentHashMap<String, Object> errorResponse = new ConcurrentHashMap<>();
    errorResponse.put("errorCode", errorCode);
    errorResponse.put("message", message);
        
     // Optionally, you could add a timestamp
     errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
     // Convert the map to JSON string (you might want to use a library like Jackson or Gson for this)
     return new Gson().toJson(errorResponse);
    }

}

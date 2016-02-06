package org.dogepool.practicalrx.error;

import java.net.SocketTimeoutException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

/**
 * The global controller advice for handling errors and returning appropriate JSON.
 */
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DogePoolException.class)
    public ModelAndView dogePoolGenericHandler(DogePoolException e, HttpServletResponse response) {
        response.setStatus(e.httpStatus.value());
        ModelAndView mav = new ModelAndView("errorWithDetail");
        mav.addObject("httpStatus", e.httpStatus);
        mav.addObject("errorCategory", e.errorCategory);
        mav.addObject("msg", e.getMessage());
        mav.addObject("errorCode", e.errorCode);
        //FIXME the exception could be logged here
        return mav;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ModelAndView externalCallErrorHandler(HttpClientErrorException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
        ModelAndView mav = new ModelAndView("errorWithDetail");
        mav.addObject("httpStatus", response.getStatus());
        mav.addObject("errorCategory", ErrorCategory.EXTERNAL_SERVICES);
        mav.addObject("msg", "External failure: " + e.getResponseBodyAsString());
        mav.addObject("errorCode", "EXTERNAL_HTTP_" + e.getStatusCode());
        //FIXME the exception could be logged here
        return mav;
    }
}

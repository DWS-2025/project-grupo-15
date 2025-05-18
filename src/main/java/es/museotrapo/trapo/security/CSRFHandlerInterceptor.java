package es.museotrapo.trapo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor for handling CSRF tokens and making them accessible in the HTTP response.
 * This interceptor ensures that the CSRF token is added to the response for further use in forms or client-side scripts.
 */
class CSRFHandlerInterceptor implements HandlerInterceptor {

    /**
     * Default constructor.
     */
    CSRFHandlerInterceptor() {
    }

    /**
     * Post-processing callback after the handler method has been executed.
     * This method retrieves the CSRF token from the request and adds it to the model, making it accessible to views (e.g., Thymeleaf or JSP).
     *
     * @param request      The HTTP request object.
     * @param response     The HTTP response object.
     * @param handler      The handler (controller) that processed the request.
     * @param modelAndView The ModelAndView object, where additional data can be added for views.
     * @throws Exception If an error occurs during processing.
     */
    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        // Check if there is a model and view to modify
        if (modelAndView != null) {
            // Retrieve the CSRF token from the request attributes
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");

            // If the token exists, add it to the model for rendering in views
            if (token != null) {
                modelAndView.addObject("token", token.getToken());
            }
        }
    }
}
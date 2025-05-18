package es.museotrapo.trapo.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

class CSRFHandlerInterceptor implements HandlerInterceptor {
    CSRFHandlerInterceptor() {
    }

    /**
     * This method is called before the actual handler is executed.
     * It can be used to perform operations before the request is processed.
     *
     * @param request  The current HTTP request.
     * @param response The current HTTP response.
     * @param handler  The chosen handler to execute, or null if none was chosen at the time of the interceptor.
     * @return true to continue processing, false to abort the request.
     */
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            CsrfToken token = (CsrfToken)request.getAttribute("_csrf");
            if (token != null) {
                modelAndView.addObject("token", token.getToken());
            }
        }

    }
}

package es.museotrapo.trapo.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

class CSRFHandlerInterceptor implements HandlerInterceptor {
    CSRFHandlerInterceptor() {
    }

    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            CsrfToken token = (CsrfToken)request.getAttribute("_csrf");
            if (token != null) {
                modelAndView.addObject("token", token.getToken());
            }
        }

    }
}

package es.museotrapo.trapo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CSRFHandlerConfiguration implements WebMvcConfigurer {
    public CSRFHandlerConfiguration() {
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CSRFHandlerInterceptor());
    }
}

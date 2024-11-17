package cn.maoyanluo.anywhere.door.config;

import cn.maoyanluo.anywhere.door.config.auth.AuthHandlerInterceptor;
import cn.maoyanluo.anywhere.door.config.params.NameArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    public WebConfig(AuthHandlerInterceptor authHandlerInterceptor, NameArgumentResolver argumentResolver) {
        this.authHandlerInterceptor = authHandlerInterceptor;
        this.argumentResolver = argumentResolver;
    }

    private final AuthHandlerInterceptor authHandlerInterceptor;
    private final NameArgumentResolver argumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authHandlerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/register", "/user/login", "/user/flush_token");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(argumentResolver);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(168000);
    }

}

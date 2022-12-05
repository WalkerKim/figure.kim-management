package kim.figure.site.management.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebFlux
public class WebfluxConfiguration implements WebFluxConfigurer {
    @Value("${frontend.host:http://localhost:5173}")
    String[] frontendHost;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList(frontendHost));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);

        corsRegistry.addMapping("/**")
                .combine(configuration);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("file:/Users/walker/IdeaProjects/figure.kim/main/src/main/resources/static/assets/")
                .setCacheControl(CacheControl.noCache());
    }

}
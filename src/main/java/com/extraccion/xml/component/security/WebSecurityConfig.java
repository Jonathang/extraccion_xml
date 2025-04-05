package com.extraccion.xml.component.security;

import com.extraccion.xml.utils.Urls;
import org.springframework.aot.generate.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * Apartado de seguridad del microservicio, cualquier intento de acceso a paths que esten fuera de esta
 * configuracion, retornara 401 unauthorized
 *
 * @author Jonathan García
 * @version 1.0
 * @since 05-04-2025
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {


     /**
     * Agrega los endpoints autorizados
     *
     * @param authorizationManagerRequestMatcherRegistry
     * @return registro de endpoints autorizados
     *
     */
    private static AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry getMatches(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
                    authorizationManagerRequestMatcherRegistry) {
        return authorizationManagerRequestMatcherRegistry
                .requestMatchers(Urls.MONITOR).permitAll()
                .requestMatchers(Urls.V1 + Urls.BUSQUEDAS).permitAll()
                .anyRequest().denyAll();
    }

     /**
     * Genera configuracion de seguridad para los endpoints permitidos
     *
     * @param http
     * @return {@linkplain SecurityFilterChain}
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(WebSecurityConfig::getMatches)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Configuración de CORS (ajustar según necesidades)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}


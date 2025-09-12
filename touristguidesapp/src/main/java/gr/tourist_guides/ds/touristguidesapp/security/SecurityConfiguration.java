package gr.tourist_guides.ds.touristguidesapp.security;

import gr.tourist_guides.ds.touristguidesapp.authentication.JwtAuthenticationFilter;
import gr.tourist_guides.ds.touristguidesapp.core.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
            throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(myCustomAuthenticationEntryPoint()))
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(myCustomAccessDeniedHandler()))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/auth/authenticate",                       // authentication
                                         "/api/guides",                                  // register Guide
                                         "/api/guides/check-username-exists/**",         // used in register Guide
                                         "/api/guides/check-record-number-exists/**",    // used in register Guide
                                         "/api/visitors",                                // register Visitor
                                         "/api/visitors/check-username-exists/**",       // used in register Visitor
                                         "/api/regions/sorted",                          // get All Regions Sorted
                                         "/api/languages/sorted",                        // get All Languages Sorted
                                         "/api/categories/sorted"                        // get All Categories Sorted
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/regions",                 // get All Regions
                                                         "/api/languages",               // get All Languages
                                                         "/api/categories"               // get All Categories
                        ).permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/guides/**").hasAnyAuthority(Role.GUIDE.name(), Role.SUPER_ADMIN.name())          // update Guide
                        .requestMatchers(HttpMethod.DELETE, "/api/guides/**").hasAnyAuthority(Role.GUIDE.name(), Role.SUPER_ADMIN.name())       // delete Guide
                        .requestMatchers("/api/guides/**").hasAnyAuthority(Role.GUIDE.name(), Role.VISITOR.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("/api/visitors/search/**").hasAnyAuthority(Role.SUPER_ADMIN.name())                // get Visitors
                        .requestMatchers("/api/visitors/**").hasAnyAuthority(Role.VISITOR.name(), Role.SUPER_ADMIN.name())  // update-delete-get Visitor
                        .requestMatchers("/api/activities").hasAnyAuthority(Role.SUPER_ADMIN.name())                        // insert Activity
                        .requestMatchers(HttpMethod.PUT, "/api/activities/**").hasAnyAuthority(Role.SUPER_ADMIN.name())     // update Activity
                        .requestMatchers(HttpMethod.DELETE, "/api/activities/**").hasAnyAuthority(Role.SUPER_ADMIN.name())  // delete Activity
                        .requestMatchers("/api/activities/**").hasAnyAuthority(Role.GUIDE.name(), Role.SUPER_ADMIN.name())  // get Activity - Activities
                        .requestMatchers("/api/regions/name/**",
                                         "/api/languages/name/**"
                        ).hasAnyAuthority(Role.GUIDE.name(), Role.VISITOR.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("/api/categories/name/**").hasAnyAuthority(Role.VISITOR.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers("/api/regions",
                                         "/api/regions/**",
                                         "/api/languages",
                                         "/api/languages/**",
                                         "/api/categories",
                                         "/api/categories/**"
                        ).hasAnyAuthority(Role.SUPER_ADMIN.name())  // static data - only SUPER_ADMIN can access
                        .requestMatchers("/**").permitAll()
                )
                .sessionManagement((session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("https://ds.tourist-guides.gr", "http://localhost:4200"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    // custom handlers for Spring Security's unauthorized (401) and forbidden (403) responses.
    @Bean
    public AuthenticationEntryPoint myCustomAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler myCustomAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}

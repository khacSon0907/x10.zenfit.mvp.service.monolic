package x10.zenfit.mvp.service.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import x10.zenfit.mvp.service.config.jwt.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${app.frontend-url:https://polo-man.vercel.app}")
    private String frontendUrl;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://polo-man.vercel.app"
        ));

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Set-Cookie"
        ));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // OAuth2 Google cần session tạm để lưu state khi redirect qua Google
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        // Cho phép preflight request của CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Google OAuth2 endpoints của Spring Security
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // Auth public APIs
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/verify-otp",
                                "/api/auth/forgot-password",
                                "/api/auth/verify-forgot-password-otp",
                                "/api/auth/reset-password",
                                "/api/auth/refresh",
                                "/api/auth/oauth2/exchange"
                        ).permitAll()

                        // Categories public read
                        .requestMatchers(HttpMethod.GET, "/api/categories/**")
                        .permitAll()

                        // Categories create/update/delete chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/categories")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        // Product admin read
                        .requestMatchers(HttpMethod.GET, "/api/products/admin/cursor")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/admin/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        // Products public read
                        .requestMatchers(HttpMethod.GET, "/api/products/**")
                        .permitAll()

                        // Products create/update/delete chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/products")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        // Banners public read
                        .requestMatchers(HttpMethod.GET, "/api/banners/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/promotion-banners/**")
                        .permitAll()

                        // Locations public read
                        .requestMatchers(HttpMethod.GET, "/api/locations/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shipping-rules/active")
                        .permitAll()
                        .requestMatchers("/ws", "/ws/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments/payos-webhook")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vouchers/available")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vouchers/apply")
                        .permitAll()
                        .requestMatchers("/api/chat/guest/**")
                        .permitAll()
                        .requestMatchers("/api/chat/support/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/chat/**")
                        .authenticated()

                        // Orders
                        .requestMatchers(HttpMethod.POST, "/api/orders")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders/guest/lookup")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/orders/lookup")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/orders/lookup/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/orders/cancel-reasons")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/orders/me/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/orders/admin/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/orders/*/delivery-note")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/orders/user/**")
                        .hasAnyRole("ADMIN", "STAFF", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/orders")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/confirm")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/cancel")
                        .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/orders/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/revenue/targets/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/revenue/targets")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/revenue/targets")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/revenue/targets/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/revenue/targets/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/revenue/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/refund-request")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/my-refunds")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/refunds/admin/cursor")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/refunds")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/refunds/*/approve")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/refunds/*/reject")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/refunds/*/mark-refunded")
                        .hasAnyRole("ADMIN", "STAFF")

                        // Banners create/update/delete chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/banners")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/banners/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/banners/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(HttpMethod.POST, "/api/promotion-banners")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/promotion-banners/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/promotion-banners/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers("/api/shipping-rules/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/point-rules")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/point-rules/**")
                        .hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/point-rules")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/point-rules/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/point-rules/**")
                        .hasRole("ADMIN")

                        // Các API auth còn lại cần login
                        .requestMatchers("/api/auth/**")
                        .authenticated()

                        // User API cần login
                        .requestMatchers(HttpMethod.GET, "/api/users/me")
                        .authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/users/me/update")
                        .authenticated()

                        // Chỉ ADMIN xem toàn bộ user
                        .requestMatchers(HttpMethod.GET, "/api/users/all")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/lock")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/unlock")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/roles")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/roles/**")
                        .hasRole("ADMIN")

                        // Những request còn lại cần login
                        .anyRequest()
                        .authenticated()
                )

                // Bật Google OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureUrl(getFrontendUrl() + "/login?error=oauth2")
                )

                // JWT filter cho các API dùng Bearer token
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private String getFrontendUrl() {
        if (StringUtils.hasText(frontendUrl)) {
            return frontendUrl;
        }

        return "https://polo-man.vercel.app";
    }
}

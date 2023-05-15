package com.pair.website.configuration;

import com.pair.website.jwt.AccessDeniedHandlerException;
import com.pair.website.jwt.AuthenticationEntryPointException;
import com.pair.website.jwt.TokenProvider;
import com.pair.website.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;
    private final CorsFilter corsFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());

        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandlerException)
                .and()
                // 인증
                .authorizeRequests()
                // antMathcers.permitAll 인증없이 허가
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/api/board/all").permitAll()
                .antMatchers("/api/board/detail/**").permitAll()
                .antMatchers("/api/oauth/token/**").permitAll()
                // 그 이외의 모든 요청 인증 후 인가
                //.anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService))
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilter(corsFilter);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //허용할 url 설정
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://codingmate-callmejeje.vercel.app");

        //허용할 헤더 설정
        configuration.addAllowedHeader("*");
        //허용할 http method
        configuration.addAllowedMethod("*");

        //사용자 자격 증명이 지원되는지 여부
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}

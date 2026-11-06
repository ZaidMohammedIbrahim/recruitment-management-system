
        package com.zaid.recruitment.Auth;

import com.zaid.recruitment.Service.CandidateService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

        @Configuration
        @EnableMethodSecurity
        @EnableWebSecurity

        public class SecurityConfig {
    JwtFilter jwtFilter;
    SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/user/**").permitAll().requestMatchers("/generateToken").permitAll()
                        .anyRequest().authenticated());

              //  .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean(UserService userService,PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }



// found during testing that the JwtFilter was getting registered twice once with Servlet container filters: Registered directly with the servlet container, such as Tomcat. and second time with our SecurityFilterChain here http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
// this was causing problem as filter was running before SecurityFilter by Servlet and setting SecurityContext , but when SecurityFilter wanted it to run it was not running again thus setting SecurityContext back to anonymous, thus causing authorization problem
// this bean below prevents JwtFilter from getting registered with Servlet
            @Bean
            public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(
                    JwtFilter jwtFilter) {

                FilterRegistrationBean<JwtFilter> registration =
                        new FilterRegistrationBean<>(jwtFilter);

                registration.setEnabled(false);
                return registration;
            }

}

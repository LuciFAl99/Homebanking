package com.mindhub.Homebanking.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization {
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

       http.authorizeRequests()
               .antMatchers("/manager.html").hasAuthority("ADMIN")
               .antMatchers("/rest/**").hasAnyAuthority("ADMIN", "CLIENT")
               .antMatchers("/h2-console/**").hasAnyAuthority("ADMIN")
               .antMatchers("/Web/index.html", "/Web/Assets/**", "/Web/login.html", "/Web/registro.html", "/Web/BigWing/**").permitAll()
               .antMatchers(HttpMethod.POST, "/api/clients/**").permitAll()
               .antMatchers(HttpMethod.POST ,"/api/login", "/api/logout").permitAll()
               .antMatchers(HttpMethod.POST, "/api/clients/current/accounts/**", "/api/clients/current/cards/**", "/api/clients/current/transactions/**", "/api/clients/current/loans/**",  "/api/loans/**").hasAuthority("CLIENT")
               .antMatchers( "/api/clients/current/accounts/**", "/api/clients/current/cards/**", "/api/clients/current/transactions/**", "/api/clients/current", "/api/clients/current/loans/**", "/api/loans/**").hasAuthority("CLIENT")
               .antMatchers("/Web/**").hasAuthority("CLIENT")
               .antMatchers("/api/accounts/{id}").hasAnyAuthority("CLIENT", "ADMIN")
               .anyRequest().authenticated();



        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");



        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }
}
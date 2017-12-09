package com.gavin.gateway.config;

import com.gavin.common.client.user.UserClient;
import com.gavin.common.service.CustomUserDetailsService;
import com.gavin.gateway.security.CustomAuthenticationEntryPoint;
import com.gavin.gateway.security.CustomAuthenticationFailureHandler;
import com.gavin.gateway.security.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/ping").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .permitAll();

//        http
//                .antMatcher("*").authorizeRequests() // all requests are protected by default
//                .antMatchers("/", "/login**", "/webjars*").permitAll() // the home page and login endpoints are explicitly excluded
//                .anyRequest().authenticated() // all other endpoints require an authenticated user
//                .and()
//                .csrf().disable();

//        http
//                .formLogin().loginPage("/login").permitAll()
//                .and()
//                .requestMatchers()
//                .antMatchers("/", "/login", "/oauth/authorize", "/oauth/confirm_access")
//                .and()
//                .authorizeRequests()
//                .anyRequest().authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        auth.inMemoryAuthentication()
//                .withUser("gavin")
//                .password("gavin")
//                .authorities("READ")
//                .and()
//                .withUser("gaven")
//                .password("gaven")
//                .authorities("READ", "WRITE");

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean(name = "customUserDetailsService")
    public CustomUserDetailsService customUserDetailsService(UserClient userClient) {
        return new CustomUserDetailsService(userClient);
    }

}

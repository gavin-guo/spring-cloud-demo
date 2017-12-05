package com.gavin.business.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {/**/
        http
                .authorizeRequests()
                .antMatchers("/users/**").hasAuthority("AUTHORITY_SUPER")
                .antMatchers("/addresses/**").hasAuthority("AUTHORITY_SUPER")
                .antMatchers("/points/**").hasAuthority("AUTHORITY_SUPER")
                .antMatchers("/products/**").hasAuthority("AUTHORITY_SUPER")
                .antMatchers("/orders/**").hasAuthority("AUTHORITY_SUPER")
                .antMatchers("/payments/**").hasAuthority("AUTHORITY_SUPER")
                .antMatchers("/deliveries/**").hasAuthority("AUTHORITY_SUPER")
                .anyRequest().authenticated();

/*        http
                .requestMatchers().antMatchers("/photos*//**", "/oauth/users*//**", "/oauth/clients*//**", "/me")
         .and()
         .authorizeRequests()
         .antMatchers("/me").access("#oauth2.hasScope('read')")
         .antMatchers("/photos").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
         .antMatchers("/photos/trusted*//**").access("#oauth2.hasScope('trust')")
         .antMatchers("/photos/user*//**").access("#oauth2.hasScope('trust')")
         .antMatchers("/photos*//**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
         .regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
         .access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
         .regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
         .access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
         .regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
         .access("#oauth2.clientHasRole('ROLE_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')");*/

/*        http
                .antMatcher("*")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars*").permitAll()
                .anyRequest().authenticated();*/
    }

}

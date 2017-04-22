package com.gavin.config;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

//@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

//    private final ResourceServerProperties resourceServerProperties;
//
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public ResourceServerConfiguration(
//            ResourceServerProperties resourceServerProperties,
//            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
//        this.resourceServerProperties = resourceServerProperties;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().authenticated();
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
//        userAuthenticationConverter.setUserDetailsService(userDetailsService);
//
//        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
//
//        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
//        remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
//        remoteTokenServices.setClientId(resourceServerProperties.getClientId());
//        remoteTokenServices.setClientSecret(resourceServerProperties.getClientSecret());
//
//        resources.tokenServices(remoteTokenServices);
//    }

}

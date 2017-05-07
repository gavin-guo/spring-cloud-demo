package com.gavin.config;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CustomFeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return template -> {
            template.header("X-USER", "aaaaaaa");
            System.out.println("-----" + template.url());
        };
    }

//    @Bean
//    @ConditionalOnBean(OAuth2ClientContext.class)
//    @ConditionalOnClass({Feign.class})
//    @ConditionalOnProperty(value = "feign.oauth2.enabled", matchIfMissing = true)
//    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
//        return new OAuth2FeignRequestInterceptor(oauth2ClientContext);
//    }

//    public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
//
//        /**
//         * The authorization header name.
//         */
//        private static final String AUTHORIZATION_HEADER = "Authorization";
//
//        /**
//         * The {@code Bearer} token type.
//         */
//        private static final String BEARER_TOKEN_TYPE = "Bearer";
//
//        /**
//         * Current OAuth2 authentication context.
//         */
//        private final OAuth2ClientContext oauth2ClientContext;
//
//        /**
//         * Creates new instance of {@link OAuth2FeignRequestInterceptor} with client context.
//         *
//         * @param oauth2ClientContext the OAuth2 client context
//         */
//        public OAuth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
//            Assert.notNull(oauth2ClientContext, "Context can not be null.");
//            this.oauth2ClientContext = oauth2ClientContext;
//        }
//
//        @Override
//        public void apply(RequestTemplate template) {
//            if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
//                log.warn("The Authorization token has been already set.");
//            } else if (oauth2ClientContext.getAccessTokenRequest().getExistingToken() == null) {
//                log.warn("Can not obtain existing token for request, if it is a non secured request, ignore.");
//            } else {
//                log.debug("Constructing Header {} for Token {}.", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
//                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE,
//                        oauth2ClientContext.getAccessTokenRequest().getExistingToken().toString()));
//            }
//        }
//    }

}

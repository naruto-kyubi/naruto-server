package org.naruto.framework.security;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.naruto.framework.security.service.captcha.CaptchaRealm;
import org.naruto.framework.security.service.account.UserPasswordRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ShiroConfig {
    @Autowired
    private UserPasswordRealm userPasswordRealm;

    @Autowired
    private CaptchaRealm captchaRealm;

//    @Bean
//    public Authenticator authenticator() {
//        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
//        authenticator.setRealms(Arrays.asList(userPasswordRealm));
//        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
//        return authenticator;
//    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager  securityManager = new DefaultWebSecurityManager ();
        securityManager.setRealms(Arrays.asList(userPasswordRealm,captchaRealm));
        return securityManager;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/**", "noSessionCreation");
        return chainDefinition;
    }
}

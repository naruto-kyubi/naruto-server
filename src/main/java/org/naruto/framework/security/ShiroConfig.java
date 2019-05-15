package org.naruto.framework.security;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.naruto.framework.security.realm.CaptchaRealm;
import org.naruto.framework.security.realm.UserPasswordRealm;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

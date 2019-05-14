package org.naruto.framework.security;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.naruto.framework.security.realm.UserPasswordRealm;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    private UserPasswordRealm userPasswordRealm;

    @Bean
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(userPasswordRealm));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }



//    /**
//     * 设置过滤器
//     */
//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
//        factoryBean.setSecurityManager(securityManager);
//        Map<String, Filter> filterMap = factoryBean.getFilters();
//        //token权限验证；
//        filterMap.put("authcToken", createAuthFilter(userService));
//        //角色验证。
//        filterMap.put("anyRole", createRolesFilter());
//        factoryBean.setFilters(filterMap);
//        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
//
//        return factoryBean;
//    }

}

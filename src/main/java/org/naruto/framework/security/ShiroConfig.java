package org.naruto.framework.security;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.naruto.framework.security.service.AnyRolesAuthorizationFilter;
import org.naruto.framework.security.service.jwt.JwtAuthFilter;
import org.naruto.framework.user.domain.Permission;
import org.naruto.framework.user.domain.ResourceRole;
import org.naruto.framework.user.repository.PermissionReponsitory;
import org.naruto.framework.user.repository.ResourceRoleReponsitory;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private List<Realm> authorizingRealmList;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionReponsitory permissionReponsitory;

    @Autowired
    private ResourceRoleReponsitory resourceRoleReponsitory;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private AnyRolesAuthorizationFilter anyRolesAuthorizationFilter;

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
        securityManager.setRealms(authorizingRealmList);
        return securityManager;
    }

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager) throws Exception{
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<Filter>();

        filterRegistration.setFilter((Filter)shiroFilter(securityManager).getObject());

        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.ASYNC);

        return filterRegistration;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        //token权限验证；
        filterMap.put("jwtAuthToken", jwtAuthFilter);
        //角色验证。
        filterMap.put("anyRole", anyRolesAuthorizationFilter);
        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());

        return factoryBean;
    }

    @Bean
    public  ShiroFilterChainDefinition shiroFilterChainDefinition() {

//        List<Permission> permissions = permissionReponsitory.getPermissionsByOrderBySeqAsc();
        List<ResourceRole> permissions = (List)resourceRoleReponsitory.findAll();

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        for (ResourceRole permission : permissions) {
            chainDefinition.addPathDefinition(permission.getResourceUrl(), permission.getPermission());
        }
        chainDefinition.addPathDefinition("/login","anon");
        chainDefinition.addPathDefinition("/**","anon");
        return chainDefinition;
    }
}
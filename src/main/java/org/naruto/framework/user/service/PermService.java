package org.naruto.framework.user.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.naruto.framework.user.domain.Perm;
import org.naruto.framework.user.repository.PermReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PermService {
    @Autowired
    private PermReponsitory permReponsitory;

//    @Autowired
//    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private ShiroFilterFactoryBean shiroFilter;

    public List<Perm> getPerms(){
        return permReponsitory.getPermsByOrderBySeqAsc();
    }

    /**
     * 重新加载权限
     */
    public void updatePermission() {

        synchronized (shiroFilter) {

            AbstractShiroFilter obj = null;
            try {
                obj = (AbstractShiroFilter) shiroFilter.getObject();
            } catch (Exception e) {
                throw new RuntimeException(
                        "get ShiroFilter from shiroFilterFactoryBean error!");
            }


            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) obj.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空已有权限控制
            manager.getFilterChains().clear();

            shiroFilter.getFilterChainDefinitionMap().clear();
            shiroFilter.setFilterChainDefinitionMap(loadFilterChainDefinitions().getFilterChainMap());
            // 重新构建生成
            Map<String, String> chains = shiroFilter.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
            log.info("update perm success");
        }
    }

    public DefaultShiroFilterChainDefinition loadFilterChainDefinitions() {
        List<Perm> perms = this.getPerms();
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        for (Perm obj : perms) {
            chainDefinition.addPathDefinition(obj.getResourceUrl(), obj.getPerm());
        }
        return chainDefinition;
    }
}

package org.naruto.framework;

import org.naruto.framework.article.ArticleReadedStatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private ArticleReadedStatInterceptor articleReadedStatInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {

        //多个拦截器组成一个拦截器链
        //addPathPatterns 用于添加拦截规则
        //excludePathPatterns用于排除拦截规则
        interceptorRegistry.addInterceptor(articleReadedStatInterceptor).addPathPatterns("/v1/articles/*");
        //设置（模糊）匹配的url
        //    List<String> urlPatterns = Lists.newArrayList();
        //    urlPatterns.add("/api/v1/goods/*");
        //    urlPatterns.add("/api/v1/userinfo/*");
        super.addInterceptors(interceptorRegistry);
    }
}

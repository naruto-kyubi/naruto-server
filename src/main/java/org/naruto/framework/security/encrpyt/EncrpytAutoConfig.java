package org.naruto.framework.security.encrpyt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncrpytAutoConfig {

    @ConditionalOnMissingBean(name="encrpytService")
    @Bean
    public IEncrpyt encrpytService(){
        return new DefaltEncrpyt();
    }
}

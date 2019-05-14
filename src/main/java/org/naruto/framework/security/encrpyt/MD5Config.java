package org.naruto.framework.security.encrpyt;

import org.springframework.context.annotation.Bean;

//@Configuration
public class MD5Config {
    @Bean
    public IEncrpyt encrpytService(){
        return new MD5Encrpyt();
    }
}

package org.naruto.framework.security.encrpyt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@ConditionalOnMissingBean(name="encrpytService")
public class DefaltEncrpyt implements IEncrpyt{

    @Override
    public String encrpyt(String str, String salt) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}

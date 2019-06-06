package org.naruto.framework.core.encrpyt;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(name="encrpytService")
public class DefaltEncrpyt implements IEncrpyt{

    @Override
    public String encrpyt(String str, String salt) {
        return new Md5Hash(str, salt).toString();
    }
}

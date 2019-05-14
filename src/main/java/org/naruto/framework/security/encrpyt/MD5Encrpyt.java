package org.naruto.framework.security.encrpyt;

import org.springframework.stereotype.Component;

@Component("encrpytService")
public class MD5Encrpyt implements IEncrpyt{

    @Override
    public String encrpyt(String str, String salt) {
        return "md5";
    }
}

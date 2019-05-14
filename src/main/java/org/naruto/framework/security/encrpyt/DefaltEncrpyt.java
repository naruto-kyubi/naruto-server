package org.naruto.framework.security.encrpyt;

import org.springframework.util.DigestUtils;

public class DefaltEncrpyt implements IEncrpyt{

    @Override
    public String encrpyt(String str, String salt) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}

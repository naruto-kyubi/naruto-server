package org.naruto.framework.security.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.DigestUtils;

@Slf4j
public class PasswordCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken userpasswordToken = (UsernamePasswordToken)token;
        String password = String.valueOf(userpasswordToken.getPassword());
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());

        if(md5.equals(info.getCredentials())) return true;
        return false;
    }
}

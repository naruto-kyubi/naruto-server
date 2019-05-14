package org.naruto.framework.security.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.naruto.framework.security.encrpyt.IEncrpyt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordCredentialsMatcher implements CredentialsMatcher {
    @Autowired
    private IEncrpyt encrpytService ;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken userpasswordToken = (UsernamePasswordToken)token;
        String password = String.valueOf(userpasswordToken.getPassword());

        ByteSource salt = ((SimpleAuthenticationInfo)info).getCredentialsSalt();

        String pwdSalt = encrpytService.encrpyt(password,salt.toString());

        if(pwdSalt.equals(info.getCredentials())) return true;
        return false;
    }
}

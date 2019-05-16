package org.naruto.framework.security.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.error.SecurityError;

public class CaptchaToken implements HostAuthenticationToken, ValidateToken {
    private static final long serialVersionUID = 9217639903967592166L;

    private String token;
    private String host;

    private String mobile;

    public CaptchaToken(String mobile, String token) {
        this(mobile, token, null);
    }

    public CaptchaToken(String mobile, String token, String host) {
        this.mobile = mobile;
        this.token = token;
        this.host = host;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return this.token;
    }

    public String getHost() {
        return host;
    }

    @Override
    public Object getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString() {
        return token + ':' + host;
    }

    @Override
    public void validate() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(this);
        } catch (AuthenticationException e) {
            throw new ServiceException(SecurityError.LOGON_MOBILE_INCORRECT_ERROR);
        }
    }


}

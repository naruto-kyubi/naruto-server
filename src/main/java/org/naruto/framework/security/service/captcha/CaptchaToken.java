package org.naruto.framework.security.service.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaToken implements AuthenticationToken {
    private static final long serialVersionUID = 9217639903967592166L;
    private String mobile;
    private String captcha;

    @Override
    public Object getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return captcha;
    }

}

package org.naruto.framework.security.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogonUser {
    private String authType;
    private String userName;
    private String password;
    private String mobile;
    private String captcha;
    private String authCode;

    /*bind (weibo/weixin/taobao)*/
    private String bindType;
    private String bindUid;
    private String bindName;

}
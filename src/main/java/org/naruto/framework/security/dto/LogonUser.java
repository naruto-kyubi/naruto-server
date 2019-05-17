package org.naruto.framework.security.dto;

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
}
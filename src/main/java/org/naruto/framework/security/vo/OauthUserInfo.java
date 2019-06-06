package org.naruto.framework.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthUserInfo {
    private String uid;
    private String name;
    private String avatar;
}

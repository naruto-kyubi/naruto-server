package org.naruto.framework.security.service;

import org.naruto.framework.security.vo.LogonUser;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;

public interface IAuthenticationService {

    User authenticate(LogonUser logonUser);

    ThirdPartyUser bind(User user, String bindType, String bindUid, String bindName);

    ThirdPartyUser bind(User user, String bindType, String authCode);

    void unbind(User user, String authType);
}

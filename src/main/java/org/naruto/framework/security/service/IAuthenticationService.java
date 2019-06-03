package org.naruto.framework.security.service;

import org.naruto.framework.security.vo.LogonUser;
import org.naruto.framework.user.domain.User;

public interface IAuthenticationService {

    User authenticate(LogonUser logonUser);

    void bind(User user,String bindType, String bindUid,String bindName);
}

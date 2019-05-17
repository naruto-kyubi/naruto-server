package org.naruto.framework.security.service;

import org.naruto.framework.security.dto.LogonUser;
import org.naruto.framework.user.domain.User;

public interface IAuthenticationService {
    User authenticate(LogonUser logonUser);
}

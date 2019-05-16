package org.naruto.framework.security.realm;

import org.naruto.framework.user.domain.User;

public interface ValidateToken {
    void validate();
    Object getPrincipal();
}

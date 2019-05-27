package org.naruto.framework.security.service;

import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.security.vo.LogonUser;
import org.naruto.framework.security.exception.SecurityError;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogonService {
    @Autowired
    private Map<String, IAuthenticationService> authenticationServiceMap = new ConcurrentHashMap<>();

    public User authenticate(LogonUser logonUser){
        String type = logonUser.getAuthType();
        if(null==type) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);

        IAuthenticationService authenticationService = authenticationServiceMap.get(type.concat("AuthenticationService"));
        if(null==authenticationService) throw new ServiceException(SecurityError.PARAMETER_VALIDATION_ERROR);

        return authenticationService.authenticate(logonUser);
    }
}

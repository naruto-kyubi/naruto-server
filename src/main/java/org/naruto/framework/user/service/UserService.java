package org.naruto.framework.user.service;

import org.naruto.framework.core.exception.EmServiceError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user){
        if(user == null) {
            throw new ServiceException(EmServiceError.PARAMETER_VALIDATION_ERROR);
        }
        return userRepository.save(user);
    }
}

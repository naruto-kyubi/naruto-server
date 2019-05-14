package org.naruto.framework.user.service;

import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.exception.UserError;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserRepository userRepository;

    private String salt = "kyubi";

    public User register(User user){
        if(user == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }
        if(null == userRepository.getUserByMobile(user.getMobile())){
            throw new ServiceException(UserError.USER_EXIST_ERROR);
        }
        if(userRepository.getUsersByNickname(user.getNickname()).size() > 0){
            throw new ServiceException(UserError.NICKNAME_EXIST_ERROR);
        }
        captchaService.verfiyCaptcha(user.getMobile(),user.getCaptcha());

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        return userRepository.save(user);
    }

    public User getUserByMobile(String mobile){
        return userRepository.getUserByMobile(mobile);
    }

}

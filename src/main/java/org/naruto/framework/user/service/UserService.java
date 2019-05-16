package org.naruto.framework.user.service;

import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.encrpyt.IEncrpyt;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.exception.UserError;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IEncrpyt encrpytService;

    @Value("${naruto.encrpyt.salt}")
    private String salt;

    public User register(User user){
        if(user == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }
        if(null != userRepository.getUserByMobile(user.getMobile())){
            throw new ServiceException(UserError.USER_EXIST_ERROR);
        }
        if(userRepository.getUsersByNickname(user.getNickname()).size() > 0){
            throw new ServiceException(UserError.NICKNAME_EXIST_ERROR);
        }
        captchaService.validateCaptcha(user.getMobile(), CaptchaType.SINGUP,user.getCaptcha());

        user.setPassword(encrpytService.encrpyt(user.getPassword(),salt));
        return userRepository.save(user);
    }

    public User getUserByMobile(String mobile){
        return userRepository.getUserByMobile(mobile);
    }


    public User resetPassword(User user){
        if(user == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }

        User current = userRepository.getUserByMobile(user.getMobile());
        if(null == current){
            throw new ServiceException(UserError.USER_NOT_EXIST_ERROR);
        }

       // captchaService.validateCaptcha(user.getMobile(), CaptchaType.FORGOTPASSWORD,user.getCaptcha());
        current.setPassword(encrpytService.encrpyt(user.getPassword(),salt));
        return userRepository.save(current);
    }

}

package org.naruto.framework.user.service;

import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.encrpyt.IEncrpyt;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.user.domain.Role;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.exception.UserError;
import org.naruto.framework.user.repository.ThirdPartyUserRepository;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ThirdPartyUserService {

    @Autowired
    private ThirdPartyUserRepository thirdPartyUserRepository;

    public void save(ThirdPartyUser thirdPartyUser){
        thirdPartyUserRepository.save(thirdPartyUser);
    }

    public ThirdPartyUser findThirdPartyUserByTypeAndUid(String type,String uid){
        return thirdPartyUserRepository.findThirdPartyUserByTypeAndUid(type,uid);
    }

}

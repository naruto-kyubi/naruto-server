package org.naruto.framework.user.service;

import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.ThirdPartyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ThirdPartyUserService {

    @Autowired
    private ThirdPartyUserRepository thirdPartyUserRepository;

    public ThirdPartyUser save(ThirdPartyUser thirdPartyUser){
        return thirdPartyUserRepository.save(thirdPartyUser);
    }

    public ThirdPartyUser findThirdPartyUserByAuthTypeAndUid(String type,String uid){
        return thirdPartyUserRepository.findThirdPartyUserByAuthTypeAndUid(type,uid);
    }

    public List<ThirdPartyUser> findThirdPartyUsersByUser(User user){
        return  thirdPartyUserRepository.findThirdPartyUsersByUser(user);
    }

    @Transactional
    public void unbind(User user, String authType){
        thirdPartyUserRepository.deleteThirdPartyUsersByUserAndAuthType(user,authType);
    }

}

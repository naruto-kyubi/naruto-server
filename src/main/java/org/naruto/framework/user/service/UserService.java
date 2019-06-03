package org.naruto.framework.user.service;

import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.encrpyt.IEncrpyt;
import org.naruto.framework.user.domain.Role;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.exception.UserError;
import org.naruto.framework.user.repository.UserRepository;
import org.naruto.framework.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

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

    public User save(User user){
        return userRepository.save(user);
    }

    public User getUserByMobile(String mobile){
        return userRepository.getUserByMobile(mobile);
    }

    public User getUserByWeibo(String weibo){
        return userRepository.getUserByWeiboUid(weibo);
    }

    public User resetPassword(User user){
        if(user == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }

        User current = userRepository.getUserByMobile(user.getMobile());
        if(null == current){
            throw new ServiceException(UserError.USER_NOT_EXIST_ERROR);
        }

        captchaService.validateCaptcha(user.getMobile(), CaptchaType.FORGOTPASSWORD,user.getCaptcha());
        current.setPassword(encrpytService.encrpyt(user.getPassword(),salt));
        return userRepository.save(current);
    }

    @Transactional
    public Set<Role> getUserRoles(String id) {
        return userRepository.findById(id).get().getRoles();
    }

    @Transactional
    public User getUserById(String id){ return this.userRepository.findById(id).get(); }

    public Page<User> queryPage(Map map) {
        return userRepository.queryPageByCondition(map);
    }

    //删除单条记录；
    public void delete(String id ){
        this.userRepository.deleteById(id);
    }

    //删除多选记录；
    public void delete(List<String> idList ){
        for(String id:idList) {
            this.userRepository.deleteById(id);
        }
    }

    //查找单条记录；
    public User findById(String id){
        return this.userRepository.findById(id).get();
    }

    public Page<User> queryCustomPage(Map map) {
        return userRepository.queryPageByCondition(map);
    }



}

package org.naruto.framework.captcha.repository;

import org.naruto.framework.captcha.domain.Captcha;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CaptchaRepository extends CrudRepository<Captcha,String> {
    Captcha findFirstByMobileAndCaptchaOrderByCreateAtDesc(String mobile,String captcha);
}

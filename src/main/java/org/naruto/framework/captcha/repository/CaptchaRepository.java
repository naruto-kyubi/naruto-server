package org.naruto.framework.captcha.repository;

import org.naruto.framework.captcha.domain.Captcha;
import org.springframework.data.repository.CrudRepository;

public interface CaptchaRepository extends CrudRepository<Captcha,String> {
}

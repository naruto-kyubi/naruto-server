package org.naruto.framework.captcha.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.captcha.CaptchaConfig;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.error.CaptchaError;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@Data
public class CaptchaService {

    @Autowired
    CaptchaRepository captchaRepository;

    @Autowired
    private CaptchaConfig captchaConfig;

    public Captcha getCaptcha(String mobile){
        Captcha captcha = new Captcha();
        captcha.setMobile(mobile);
        captcha.setCreateAt(new Date());

        Random random = new Random();
        String otp = Integer.toString(1000 + random.nextInt(9000));

        log.info("The otp code is :" + otp);
        captcha.setCaptcha(otp);
        sendSMS(mobile, otp);
        return captchaRepository.save(captcha);
    }

    public void verfiyCaptcha(String mobile, String captcha){
        if(StringUtils.isBlank(mobile) ||  StringUtils.isBlank(captcha)) throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);

        Captcha otp = captchaRepository.findFirstByMobileAndCaptchaOrderByCreateAtDesc(mobile,captcha);
        if(null==otp)  throw new ServiceException(CaptchaError.CAPTCHA_INCORRECT_ERROR);
        long duration = Duration.between(otp.getCreateAt().toInstant(), Instant.now()).getSeconds();
        if (duration > 300) throw new ServiceException(CaptchaError.CAPTCHA_TIMEOUT_ERROR);
    }

    private void sendSMS(String mobile, String content) {
        DefaultProfile profile = DefaultProfile.getProfile(captchaConfig.getRegionId(), captchaConfig.getAccessKey(), captchaConfig.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain(captchaConfig.getDomain());
        request.setVersion(captchaConfig.getVersion());
        request.setAction("SendSms");
        request.putQueryParameter("RegionId",captchaConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", captchaConfig.getSignName());
        request.putQueryParameter("TemplateCode", captchaConfig.getTemplateCode());

        request.putQueryParameter("TemplateParam", "{\"code\":" + String.valueOf(content) + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
            JSONObject jsonObject = JSONObject.parseObject(response.getData());
            String code = jsonObject.getString("Code");
            if(!"OK".equals(code)) throw new ServiceException(CaptchaError.CAPTCHA_SERVICE_ERROR);
        } catch (ServerException e) {
            log.error(e.getErrMsg());
            throw new ServiceException(CaptchaError.CAPTCHA_SERVICE_ERROR);
        } catch (ClientException e) {
            log.error(e.getErrMsg());
            throw new ServiceException(CaptchaError.CAPTCHA_SERVICE_ERROR);
        }
    }


}

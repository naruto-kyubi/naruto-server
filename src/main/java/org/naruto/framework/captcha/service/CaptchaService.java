package org.naruto.framework.captcha.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.captcha.CaptchaConfig;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.naruto.framework.core.exception.EmServiceError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        int otp = 1000 + random.nextInt(999);

        captcha.setCaptcha(String.valueOf(otp));
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

        request.putQueryParameter("TemplateParam", "{\"code\":" + String.valueOf(otp) + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
            JSONObject jsonObject = JSONObject.parseObject(response.getData());
            String code = jsonObject.getString("Code");
            if(!"OK".equals(code)) throw new ServiceException(EmServiceError.CAPTCHA_SERVICE_ERROR);
            return captchaRepository.save(captcha);
        } catch (ServerException e) {
            log.error(e.getErrMsg());
            throw new ServiceException(EmServiceError.CAPTCHA_SERVICE_ERROR);
        } catch (ClientException e) {
            log.error(e.getErrMsg());
            throw new ServiceException(EmServiceError.CAPTCHA_SERVICE_ERROR);
        }
    }
}

package org.naruto.framework.captcha.service;

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
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@Data
public class CaptchaService {

    @Autowired
    CaptchaRepository captchaRepository;

    @Value("${captcha.region-id}")
    private String regionId;

    @Value("${captcha.domain}")
    private String domain;

    @Value("${captcha.accessKey}")
    private String accessKey;

    @Value("${captcha.accessSecret}")
    private String accessSecret;

    @Value("${captcha.version}")
    private String version;

    @Value("${captcha.sign-name}")
    private String signName;

    @Value("${captcha.template-code}")
    private String templateCode;

    public Captcha getCaptcha(String mobile){
        Captcha captcha = new Captcha();
        captcha.setMobile(mobile);
        captcha.setCreateAt(new Date());

        Random random = new Random();
        int otp = 1000 + random.nextInt(999);

        captcha.setCaptcha(String.valueOf(otp));

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion(version);
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":" + String.valueOf(otp) + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
        } catch (ServerException e) {
            log.error(e.getErrMsg());
        } catch (ClientException e) {
            log.error(e.getErrMsg());
        }

        return captchaRepository.save(captcha);
    }
}

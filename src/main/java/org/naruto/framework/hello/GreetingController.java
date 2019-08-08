package org.naruto.framework.hello;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.web.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class GreetingController {

    private static final String template = "Hello, %s!!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping("/v1/greeting")
    public ResponseEntity<ResultEntity>  greeting(@RequestParam(value="name") String name) {
        if(name==null || "".equals(name)) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }else {
            log.info(name + " vist the site");
            Greeting greeting = new Greeting(counter.incrementAndGet(),
                    String.format(template, name));

            Article article = new Article();
            article.setId("123456");
            article.setCover("COVER");

            ArticleVo v =  modelMapper.map(article,ArticleVo.class);;

            return ResponseEntity.ok(ResultEntity.ok(v));
        }
    }
}

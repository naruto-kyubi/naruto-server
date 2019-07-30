package org.naruto.framework.es;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.FrameworkApplication;
import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.search.user.repository.UserEsRepository;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.naruto.framework.common.utils.ObjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkApplication.class)
public class UserEsRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEsRepository userEsRepository;

    @Test
    public void exportMySql2ES(){
        List<User> userList = userRepository.findAll();
        List<EsUser> esUserList = ObjUtils.transformerClass(userList,EsUser.class);

        for (EsUser esUser : esUserList) {
            userEsRepository.save(esUser);
        }
    }
}

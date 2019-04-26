package org.naruto.framework;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrameworkApplicationTests {

	@Autowired
	public UserRepository userRepository;

	@Test
    public void createUser(){
		userRepository.save(new User("Jack","13034196846","Jack130","Jack130@gamil.com"));
	}
}

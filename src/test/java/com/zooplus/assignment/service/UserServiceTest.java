package com.zooplus.assignment.service;

import com.zooplus.assignment.application.Beans;
import com.zooplus.assignment.application.ServiceConfig;
import com.zooplus.assignment.application.TestDbConfig;
import com.zooplus.assignment.persistence.dao.UserRepository;
import com.zooplus.assignment.web.dto.UserDto;
import com.zooplus.assignment.web.exceptions.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestDbConfig.class, ServiceConfig.class, Beans.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void cannotRegister2UsersWithSameEmail() {
        UserDto dto = new UserDto();
        dto.setEmail("john.doe@foo.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("password");
        userService.registerAccount(dto);

        // second call will cause exception
        userService.registerAccount(dto);
    }
}

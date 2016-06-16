package com.zooplus.assignment.service;

import com.zooplus.assignment.persistence.dao.UserRepository;
import com.zooplus.assignment.persistence.model.User;
import com.zooplus.assignment.web.dto.UserDto;
import com.zooplus.assignment.web.exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerAccount(UserDto userDto) {

        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email adress: " + userDto.getEmail());
        }
        final User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        log.info("Successfull registered account {}", savedUser.getEmail());
        return savedUser;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private boolean emailExist(final String email) {
        final User user = userRepository.findByEmail(email);
        return null != user;
    }

}

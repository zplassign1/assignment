package com.zooplus.assignment.web.controller;

import com.zooplus.assignment.persistence.model.User;
import com.zooplus.assignment.service.UserService;
import com.zooplus.assignment.web.dto.UserDto;
import com.zooplus.assignment.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationController {
    private final static Logger log = LoggerFactory.getLogger(RegistrationController.class);


    @Autowired
    private UserService userService;


    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
        log.debug("Registering user account with information: {}", accountDto);

        final User registered = userService.registerAccount(accountDto);
        return new GenericResponse("success");
    }

}

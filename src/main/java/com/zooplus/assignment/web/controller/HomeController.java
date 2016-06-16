package com.zooplus.assignment.web.controller;

import com.zooplus.assignment.persistence.model.ExchangeRate;
import com.zooplus.assignment.persistence.model.User;
import com.zooplus.assignment.service.ExchangeRateService;
import com.zooplus.assignment.service.OpenExchange;
import com.zooplus.assignment.service.UserService;
import com.zooplus.assignment.web.model.RateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Currency;
import java.util.List;

@Controller
public class HomeController {
    private final static Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private UserService userService;

    @Autowired
    private OpenExchange openExchange;

    @RequestMapping(path = "/secure/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("rateModel",new RateModel("USD","EUR"));
        mv.addObject("history", exchangeRateService.getHistory(getCurrentUser(), 10));
        return mv;
    }

    @RequestMapping(path = "/secure/home/latest", method = RequestMethod.POST)
    public ModelAndView rate(@ModelAttribute("rateModel") RateModel rateModel) {
        log.info("Getting rate for {} to {}", rateModel.getFromCurrency(), rateModel.getToCurrency());
        ExchangeRate rate = exchangeRateService.latest(getCurrentUser(), rateModel.getFromCurrency(), rateModel.getToCurrency());
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("rateModel",rateModel);
        mv.addObject("history", exchangeRateService.getHistory(getCurrentUser(), 10));
        return mv;
    }

    @ModelAttribute("currencies")
    public List<Currency> currencies() {
        return exchangeRateService.currencies();
    }

    private User getCurrentUser() {
        return userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}

package com.zooplus.assignment.service;

import com.zooplus.assignment.application.Beans;
import com.zooplus.assignment.application.ServiceConfig;
import com.zooplus.assignment.application.TestDbConfig;
import com.zooplus.assignment.persistence.model.ExchangeRate;
import com.zooplus.assignment.persistence.model.User;
import com.zooplus.assignment.web.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestDbConfig.class, ServiceConfig.class, Beans.class})
public class ExchangeRateServiceTest {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void canSaveANewExchangeRate() throws Exception {
        assertThat(addRateToHistory("USD","EUR", registerAccount()).getId()).isNotNull();
    }

    @Test
    @Transactional
    public void addingANewRateMakesItPartOfTheHistory() throws Exception {
        User user = registerAccount();
        ExchangeRate er1 = addRateToHistory("USD", "EUR", user);
        ExchangeRate er2 = addRateToHistory("USD", "EUR", user);
        List<ExchangeRate> rates = exchangeRateService.getHistory(user, 10);
        assertThat(rates.get(0).getId()).isEqualTo(er2.getId());
        assertThat(rates.get(1).getId()).isEqualTo(er1.getId());
    }

    @Test
    @Transactional
    public void onlyMaxItemsOfHistoryAreReturned() throws Exception {
        User user = registerAccount();
        for (int i = 0; i < 5; i++) {
            addRateToHistory("USD", "EUR", user);
        }
        assertThat(exchangeRateService.getHistory(user, 3).size()).isEqualTo(3);
    }

    private ExchangeRate addRateToHistory(String from, String to, User user) {
        ExchangeRate er = new ExchangeRate();
        er.setFromCurrency("USD");
        er.setToCurrency("EUR");
        er.setUser(user);
        er.setTs(new Date());
        return exchangeRateService.addRateToHistory(er);
    }

    private User registerAccount() {
        UserDto dto = new UserDto();
        dto.setEmail("john.doe@gmail.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("password");
        return userService.registerAccount(dto);
    }
}

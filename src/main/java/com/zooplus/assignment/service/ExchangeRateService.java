package com.zooplus.assignment.service;

import com.zooplus.assignment.persistence.dao.ExchangeRateRepository;
import com.zooplus.assignment.persistence.model.ExchangeRate;
import com.zooplus.assignment.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

@Service
@Transactional
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OpenExchange openExchange;

    public ExchangeRate addRateToHistory(ExchangeRate rate) {
        return exchangeRateRepository.save(rate);
    }

    public List<ExchangeRate> getHistory(User user, int maxItems) {
        Query query = entityManager.createQuery("SELECT er FROM ExchangeRate er WHERE er.user.id = :userid ORDER BY id DESC");
        return (List<ExchangeRate>)query.setParameter("userid", user.getId()).setMaxResults(maxItems).getResultList();
    }

    public ExchangeRate latest(User user, String from, String to) {
        OpenExchange.OpenExchangeRate rate = openExchange.rate(from, to);

        return addRateToHistory(toExchangeRate(user, rate));

    }

    public List<Currency> currencies() {
        return asList(
                Currency.getInstance("USD"),
                Currency.getInstance("EUR"),
                Currency.getInstance("JPY"),
                Currency.getInstance("CHF"),
                Currency.getInstance("GBP")
        );

    }

    private ExchangeRate toExchangeRate(User user, OpenExchange.OpenExchangeRate oeRate) {
        ExchangeRate er = new ExchangeRate();
        er.setFromCurrency(oeRate.getFromCurrency());
        er.setTs(new Date());
        er.setToCurrency(oeRate.getToCurrency());
        er.setUser(user);
        er.setRate(oeRate.getRate());
        return er;
    }

}

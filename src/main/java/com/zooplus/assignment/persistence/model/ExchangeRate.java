package com.zooplus.assignment.persistence.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
    @Column(length = 3)
    private String fromCurrency;

    @Column(length = 3)
    private String toCurrency;

    private double rate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date ts;

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}

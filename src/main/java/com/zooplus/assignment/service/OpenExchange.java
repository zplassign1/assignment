package com.zooplus.assignment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OpenExchange {

    public static final String LATEST_URL = "https://openexchangerates.org/api/latest.json";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${openexchange.app_id}")
    private String appId;


    public OpenExchangeRate rate(String fromCurrency, String toCurrency) {
        Map<String,String> params = new TreeMap<>();
        params.put("app_id",appId);
        params.put("base",fromCurrency);
        params.put("symbols",toCurrency);
        OpenExchangeRateParser parser = new OpenExchangeRateParser(fromCurrency, toCurrency, mapper);
        return restTemplate.execute(LATEST_URL + "?app_id={app_id}", HttpMethod.GET, clientHttpRequest -> {}, parser::parse, params);
    }

    public static class OpenExchangeRateParser {
        private final String fromCurrency;
        private final String toCurrency;
        private final ObjectMapper mapper;

        public OpenExchangeRateParser(String fromCurrency, String toCurrency, ObjectMapper mapper) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.mapper = mapper;
        }

        public OpenExchangeRate parse(ClientHttpResponse response) throws IOException {
            JsonNode node = mapper.readTree(response.getBody());
            return new OpenExchangeRate(fromCurrency, toCurrency, node.get("rates").get(toCurrency).asDouble());
        }
    }

    public static class OpenExchangeRate {
        private final String fromCurrency;
        private final String toCurrency;
        private final double rate;

        public OpenExchangeRate(String fromCurrency, String toCurrency, double rate) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.rate = rate;
        }

        public String getFromCurrency() {
            return fromCurrency;
        }

        public double getRate() {
            return rate;
        }

        public String getToCurrency() {
            return toCurrency;
        }
    }
}

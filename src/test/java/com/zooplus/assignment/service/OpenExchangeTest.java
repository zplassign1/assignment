package com.zooplus.assignment.service;

import com.zooplus.assignment.application.Beans;
import com.zooplus.assignment.application.ServiceConfig;
import com.zooplus.assignment.application.TestDbConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Beans.class, ServiceConfig.class, TestDbConfig.class})
public class OpenExchangeTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenExchange openExchange;

    private MockRestServiceServer mockServer;

    @Before
    public void before() throws Exception {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void latest() throws Exception {
        mockServer.expect(requestTo(OpenExchange.LATEST_URL + "?app_id=test"))
                .andExpect(method(GET))
                .andRespond(withSuccess(jsonResource("usd_to_eur"), MediaType.APPLICATION_JSON));
        OpenExchange.OpenExchangeRate rate = openExchange.rate("USD","EUR");
        assertThat(rate.getRate()).isEqualTo(1.12d);
        assertThat(rate.getFromCurrency()).isEqualTo("USD");
        assertThat(rate.getToCurrency()).isEqualTo("EUR");

    }

    protected Resource jsonResource(String filename) {
        return new ClassPathResource(filename + ".json", getClass());
    }

}

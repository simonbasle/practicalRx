package org.dogepool.practicalrx.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.dogepool.practicalrx.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class RateControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRateEuro() throws Exception {
        mockMvc.perform(get("/rate/EUR").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(startsWith("{\"moneyCodeFrom\":\"DOGE\",\"moneyCodeTo\":\"EUR\",\"exchangeRate\":")));
    }

    @Test
    public void testRateBadCurrencyTooLong() throws Exception {
        //Note: the configuration of test has a timeout of 6 seconds, which will always succeed
        mockMvc.perform(get("/rate/EURO").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString("Unknown currency EURO")));
    }

    @Test
    public void testRateBadCurrencyBadCase() throws Exception {
        //Note: the configuration of test has a timeout of 6 seconds, which will always succeed
        mockMvc.perform(get("/rate/EuR").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString("Unknown currency EuR")));
    }
}
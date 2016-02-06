package org.dogepool.practicalrx.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.dogepool.practicalrx.Main;
import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class SearchControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSearchByName() throws Exception {
        String expected = "[{\"id\":1,\"nickname\":\"richUser\",\"displayName\":\"Richie Rich\"," +
                "\"bio\":\"I'm rich I have dogecoin\",\"avatarId\":\"45678\",\"type\":\"user\"}]";

        mockMvc.perform(get("/search/user/{pattern}", "richie"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    public void testSearchByNameNoResult() throws Exception {
        mockMvc.perform(get("/search/user/{pattern}", "alfred"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testSearchByCoinsMinOnly() throws Exception {
        String expected = "[{\"user\":{\"id\":1,\"nickname\":\"richUser\",\"displayName\":\"Richie Rich\"," +
                "\"bio\":\"I'm rich I have dogecoin\",\"avatarId\":\"45678\",\"type\":\"user\"}," +
                "\"hashrate\":-1.0,\"totalCoinsMined\":12}]";

        mockMvc.perform(get("/search/user/coins/{min}", 10))
               .andExpect(status().isOk())
               .andExpect(content().json(expected));
    }

    @Test
    public void testSearchByCoinsMinNoMatch() throws Exception {
        mockMvc.perform(get("/search/user/coins/{min}", 1200))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    @Test
    public void testSearchByCoinsMinMax() throws Exception {
        String expected = "[{\"user\":{\"id\":1,\"nickname\":\"richUser\",\"displayName\":\"Richie Rich\"," +
                "\"bio\":\"I'm rich I have dogecoin\",\"avatarId\":\"45678\",\"type\":\"user\"}," +
                "\"hashrate\":-1.0,\"totalCoinsMined\":12}]";

        mockMvc.perform(get("/search/user/coins/{min}/{max}", 10, 13))
               .andExpect(status().isOk())
               .andExpect(content().json(expected));
    }

    @Test
    public void testSearchByCoinsMinMaxNoMinMatch() throws Exception {
        mockMvc.perform(get("/search/user/coins/{min}/{max}", 13, 14))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    @Test
    public void testSearchByCoinsMinMaxNoMaxMatch() throws Exception {
        mockMvc.perform(get("/search/user/coins/{min}/{max}", 1, 11))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }
}
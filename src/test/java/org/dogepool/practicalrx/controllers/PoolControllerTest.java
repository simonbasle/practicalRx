package org.dogepool.practicalrx.controllers;

import static org.hamcrest.Matchers.containsString;
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
public class PoolControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLadderByHashrate() throws Exception {
        String expected = "[{\"user\":{\"id\":0,\"nickname\":\"user0\",\"displayName\":\"Test User\"," +
                "\"bio\":\"Story of my life.\\nEnd of Story.\",\"avatarId\":\"12434\",\"type\":\"user\"}," +
                "\"hashrate\":1.234,\"totalCoinsMined\":0}," +
                "{\"user\":{\"id\":1,\"nickname\":\"richUser\",\"displayName\":\"Richie Rich\"," +
                "\"bio\":\"I'm rich I have dogecoin\",\"avatarId\":\"45678\",\"type\":\"user\"}," +
                "\"hashrate\":0.11,\"totalCoinsMined\":12}]";
        System.out.println(expected);

        mockMvc.perform(get("/pool/ladder/hashrate"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expected));
    }

    @Test
    public void testLadderByCoins() throws Exception {
        String expected = "[{\"user\":{\"id\":1,\"nickname\":\"richUser\",\"displayName\":\"Richie Rich\"," +
                "\"bio\":\"I'm rich I have dogecoin\",\"avatarId\":\"45678\",\"type\":\"user\"},\"hashrate\":0.11," +
                "\"totalCoinsMined\":12},{\"user\":{\"id\":0,\"nickname\":\"user0\",\"displayName\":\"Test User\"," +
                "\"bio\":\"Story of my life.\\nEnd of Story.\"," +
                "\"avatarId\":\"12434\",\"type\":\"user\"},\"hashrate\":1.234,\"totalCoinsMined\":0}]";
        System.out.println(expected);

        mockMvc.perform(get("/pool/ladder/coins"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expected));
    }

    @Test
    public void testGlobalHashRate() throws Exception {
        String expected = "{\"hashrate\":1.234,\"unit\":\"GHash/s\"}";

        mockMvc.perform(get("/pool/hashrate"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expected));
    }

    @Test
    public void testMiners() throws Exception {
        String expected = "{\"totalMiningUsers\":1,\"totalUsers\":2}";

        mockMvc.perform(get("/pool/miners"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expected));
    }

    @Test
    public void testActiveMiners() throws Exception {
        String expected = "[{\"id\":0,\"nickname\":\"user0\",\"displayName\":\"Test User\"," +
                "\"bio\":\"Story of my life.\\nEnd of Story.\",\"avatarId\":\"12434\",\"type\":\"user\"}]";
        System.out.println(expected);

        mockMvc.perform(get("/pool/miners/active"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expected));
    }

    @Test
    public void testLastBlock() throws Exception {
        //there's some part of randomness in this one
        mockMvc.perform(get("/pool/lastblock"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().string(containsString("\"foundAgo\"")))
               .andExpect(content().string(containsString("\"foundBy\"")))
               .andExpect(content().string(containsString("\"foundOn\"")));
    }
}
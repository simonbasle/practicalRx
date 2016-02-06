package org.dogepool.practicalrx.controllers;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testRegisterMiningBadUser() throws Exception {
        mockMvc.perform(post("/admin/mining/1024"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testRegisterMiningGoodUser() throws Exception {
        mockMvc.perform(post("/admin/mining/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(startsWith("[")));
    }

    @Test
    public void testDeregisterMiningBadUser() throws Exception {
        mockMvc.perform(delete("/admin/mining/1024"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testDeregisterMiningGoodUser() throws Exception {
        mockMvc.perform(delete("/admin/mining/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(startsWith("[")));
    }

    @Test
    public void testCost() throws Exception {
        mockMvc.perform(get("/admin/cost/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"cost\"")))
                .andExpect(content().string(containsString("\"month\"")))
                .andExpect(content().string(containsString("\"currency\"")))
                .andExpect(content().string(containsString("\"currencySign\"")));

    }

    @Test
    public void testCostMonthName() throws Exception {
        mockMvc.perform(get("/admin/cost/2015/JANUARY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"cost\":2115,\"month\":\"JANUARY 2015\"," +
                        "\"currencySign\":\"$\",\"currency\":\"USD\"}"));
    }

    @Test
    public void testCostMonthBadName() throws Exception {
        mockMvc.perform(get("/admin/cost/2015/FOO"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCostMonthNumber() throws Exception {
        mockMvc.perform(get("/admin/cost/2015-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"cost\":2115,\"month\":\"JANUARY 2015\"," +
                        "\"currencySign\":\"$\",\"currency\":\"USD\"}"));
    }
}
package org.dogepool.practicalrx.controllers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.error.*;
import org.dogepool.practicalrx.error.Error;
import org.dogepool.practicalrx.services.AdminService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PoolService poolService;

    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.POST, value = "/mining/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> registerMiningUser(@PathVariable("id") long id) {
        User user = userService.getUser(id);
        if (user != null) {
            boolean connected = poolService.connectUser(user);
            List<User> miningUsers = poolService.miningUsers();
            return new ResponseEntity<>(miningUsers, HttpStatus.ACCEPTED);
        } else {
            throw new DogePoolException("User cannot mine, not authenticated", Error.BAD_USER, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "mining/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deregisterMiningUser(@PathVariable("id") long id) {
        User user = userService.getUser(id);
        if (user != null) {
            boolean disconnected = poolService.disconnectUser(user);
            List<User> miningUsers = poolService.miningUsers();
            return new ResponseEntity<>(miningUsers, HttpStatus.ACCEPTED);
        } else {
            throw new DogePoolException("User is not mining, not authenticated", Error.BAD_USER, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/cost/{year}-{month}")
    public Map<String, Object> cost(@PathVariable int year, @PathVariable int month) {
        Month monthEnum = Month.of(month);
        return cost(year, monthEnum);
    }

    @RequestMapping("/cost")
    public Map<String, Object> cost() {
        LocalDate now = LocalDate.now();
        return cost(now.getYear(), now.getMonth());
    }

    @RequestMapping("/cost/{year}/{month}")
    protected Map<String, Object> cost(@PathVariable int year, @PathVariable Month month) {
        BigInteger cost = adminService.costForMonth(year, month);

        Map<String, Object> json = new HashMap<>();
        json.put("month", month + " " + year);
        json.put("cost", cost);
        json.put("currency", "USD");
        json.put("currencySign", "$");
        return json;
    }
}

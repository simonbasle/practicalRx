package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to retrieve information on the current status of the mining pool
 */
@Service
public class PoolService {

    private final Set<User> connectedUsers = new HashSet<>();

    public String poolName() {
        return "Wow Such Pool!";
    }

    public List<User> miningUsers() {
        return new ArrayList<>(connectedUsers);
    }

    public boolean connectUser(User user) {
        connectedUsers.add(user);
        System.out.println(user.nickname + " connected");
        return true;
    }

    public boolean disconnectUser(User user) {
        connectedUsers.remove(user);
        System.out.println(user.nickname + " disconnected");
        return true;
    }
}

package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to retrieve information on the current status of the mining pool
 */
@Service
public class PoolService {

    private final Set<User> connectedUsers = new HashSet<>();

    public String poolName() {
        return "Wow Such Pool!";
    }

    public Observable<User> miningUsers() {
        return Observable.from(connectedUsers);
    }

    public Observable<Boolean> connectUser(User user) {
        return Observable.create(s -> {
            connectedUsers.add(user);
            System.out.println(user.nickname + " connected");
            s.onNext(Boolean.TRUE);
            s.onCompleted();
        });
    }

    public Observable<Boolean> disconnectUser(User user) {
        return Observable.create(s -> {
            connectedUsers.remove(user);
            System.out.println(user.nickname + " disconnected");
            s.onNext(Boolean.TRUE);
            s.onCompleted();
        });
    }
}

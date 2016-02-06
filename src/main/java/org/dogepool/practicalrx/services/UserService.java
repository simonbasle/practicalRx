package org.dogepool.practicalrx.services;

import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.Statement;
import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.error.DogePoolException;
import org.dogepool.practicalrx.error.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service to get user information.
 */
@Service
public class UserService {

    @Autowired(required = false)
    private Bucket couchbaseBucket;

    @Value("${store.enableFindAll:false}")
    private boolean useCouchbaseForFindAll;

    public User getUser(long id) {
        for (User user : findAll()) {
            if (user.id == id) {
                return user;
            }
        }

        return null; //TODO any better way of doing this in Java 8?
    }

    public User getUserByLogin(String login) {
        for (User user : findAll()) {
            if (login.equals(user.nickname)) {
                return user;
            }
        }

        return null; //TODO any better way of doing this in Java 8?
    }

    public List<User> findAll() {
        if (useCouchbaseForFindAll && couchbaseBucket != null) {
            try {
                Statement statement = Select.select("avatarId", "bio", "displayName", "id", "nickname").from(x("default"))
                                            .where(x("type").eq(s("user"))).groupBy(x("displayName"));
                N1qlQueryResult queryResult = couchbaseBucket.query(statement);
                List<User> users = new ArrayList<User>();
                for (N1qlQueryRow qr : queryResult) {
                    users.add(User.fromJsonObject(qr.value()));
                }
                return users;
            } catch (Exception e) {
                throw new DogePoolException("Error while getting list of users from database",
                        Error.DATABASE, HttpStatus.INTERNAL_SERVER_ERROR, e);
            }
        } else {
            return Arrays.asList(User.USER, User.OTHERUSER);
        }
    }
}

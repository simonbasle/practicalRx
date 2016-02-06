package org.dogepool.practicalrx.services;

import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to get user information.
 */
@Service
public class UserService {

    @Autowired(required = false)
    private Bucket couchbaseBucket;

    @Value("${store.enableFindAll:false}")
    private boolean useCouchbaseForFindAll;

    public Observable<User> getUser(long id) {
        return findAll()
                .filter(u -> u.id == id)
                .take(1);
    }

    public Observable<User> getUserByLogin(String login) {
        return findAll()
                .filter(u -> login.equals(u.nickname))
                .take(1);
    }

    public Observable<User> findAll() {
        if (useCouchbaseForFindAll && couchbaseBucket != null) {
            try {
                Statement statement = Select.select("avatarId", "bio", "displayName", "id", "nickname").from(x("default"))
                                            .where(x("type").eq(s("user"))).groupBy(x("displayName"));
                N1qlQueryResult queryResult = couchbaseBucket.query(statement);
                List<User> users = new ArrayList<User>();
                for (N1qlQueryRow qr : queryResult) {
                    users.add(User.fromJsonObject(qr.value()));
                }
                return Observable.from(users);
            } catch (Exception e) {
                return Observable.error(new DogePoolException("Error while getting list of users from database",
                        Error.DATABASE, HttpStatus.INTERNAL_SERVER_ERROR, e));
            }
        } else {
            return Observable.just(User.USER, User.OTHERUSER);
        }
    }
}

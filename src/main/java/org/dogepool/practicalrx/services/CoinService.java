package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.stereotype.Service;

/**
 * Service for getting info on coins mined by users.
 */
@Service
public class CoinService {

    public long totalCoinsMinedBy(User user) {
        if (user.equals(User.OTHERUSER)) {
            return 12L;
        } else {
            return 0L;
        }
    }
}

package org.dogepool.practicalrx.error;

public enum Error {

    UNREACHABLE_SERVICE(0, ErrorCategory.EXTERNAL_SERVICES),
    BAD_USER(1, ErrorCategory.AUTHENTICATION),
    BAD_CURRENCY(2, ErrorCategory.EXCHANGE_RATE),
    UNKNOWN_USER(3, ErrorCategory.USERS),
    RANK_HASH(4, ErrorCategory.RANKING),
    RANK_COIN(5, ErrorCategory.RANKING),
    DATABASE(100, ErrorCategory.EXTERNAL_SERVICES);

    public final int code;

    public final ErrorCategory category;

    private Error(int code, ErrorCategory category) {
        this.code = code;
        this.category = category;
    }

}

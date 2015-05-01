package at.sum.android.cysmn.utils;

/**
 * Created by widnig89 on 01.05.15.
 */
public enum FactionEnum {

    RUNNER(0),
    ONLINE_PLAYER(1),
    INSPECTOR(2); //maybe in future to attend the session as viewer/inspector

    private final int value;

    private FactionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
};

package com.funlisten.service.downNet.down;

/**
 * Created by ZY on 17/3/17.
 */

public enum ZYDownState {
    WAIT(0),
    START(1),
    DOWNING(2),
    PAUSE(3),
    STOP(4),
    ERROR(5),
    FINISH(6);
    private int state = 0;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    ZYDownState(int state) {
        this.state = state;
    }
}

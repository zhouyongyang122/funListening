package com.funlisten.base.event;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.play.model.bean.ZYAudio;

/**
 * Created by ZY on 17/7/17.
 */

public class ZYEventPlayState implements ZYIBaseBean {

    public ZYAudio audio;

    public int state;

    public int duration;

    public int currentDuration;

    public ZYEventPlayState(ZYAudio audio, int state, int duration, int currentDuration) {
        this.audio = audio;
        this.state = state;
        this.duration = duration;
        this.currentDuration = currentDuration;
    }
}

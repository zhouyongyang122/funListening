package com.funlisten.business.play.model;

import com.funlisten.business.play.model.bean.ZYAudio;

/**
 * Created by ZY on 17/7/19.
 */

public class FZAudionPlayEvent {

    public ZYAudio audio;

    //播放状态 参见FZAudioPlaySevice
    public int state;

    //播放的当前时长
    public int currentDuration;

    //音频时长
    public int totalDuration;

    String msg;

    public FZAudionPlayEvent(ZYAudio audio, int state, String msg, int currentDuration, int totalDuration) {
        this.audio = audio;
        this.currentDuration = currentDuration;
        this.totalDuration = totalDuration;
        this.msg = msg;
        this.state = state;
    }
}

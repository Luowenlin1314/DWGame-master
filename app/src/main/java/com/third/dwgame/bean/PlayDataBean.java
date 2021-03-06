package com.third.dwgame.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Sky on 2018/3/6.
 * 用途：配置相关信息
 */

public class PlayDataBean {

    //串口端口
    private String defaultSerialPort;

    //串口波特率
    private String defaultSerialRate;

    //默认播放地址
    private String defaultPlayPath;

    //广告集合
    private List<ADPlayBean> adPlayBeanList = new ArrayList<>();

    public String getDefaultSerialPort() {
        return defaultSerialPort;
    }

    public void setDefaultSerialPort(String defaultSerialPort) {
        this.defaultSerialPort = defaultSerialPort;
    }

    public String getDefaultSerialRate() {
        return defaultSerialRate;
    }

    public void setDefaultSerialRate(String defaultSerialRate) {
        this.defaultSerialRate = defaultSerialRate;
    }

    public String getDefaultPlayPath() {
        return defaultPlayPath;
    }

    public void setDefaultPlayPath(String defaultPlayPath) {
        this.defaultPlayPath = defaultPlayPath;
    }

    public List<ADPlayBean> getAdPlayBeanList() {
        if(adPlayBeanList == null){
            adPlayBeanList = new ArrayList<>();
        }
        return adPlayBeanList;
    }

    public void setAdPlayBeanList(List<ADPlayBean> adPlayBeanList) {
        this.adPlayBeanList = adPlayBeanList;
    }
}

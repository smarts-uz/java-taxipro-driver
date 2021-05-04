package com.ourdevelops.ourdriver.models;

import java.io.Serializable;

import static com.ourdevelops.ourdriver.json.fcm.FCMType.CHAT;

/**
 * Created by Ourdevelops Team on 19/10/2019.
 */
public class Chat implements Serializable{
    public int type = CHAT;
    public String senderid;
    public String receiverid;
    public String name;
    public String pic;
    public String tokendriver;
    public String tokenuser;
    public String message;
    public String isdriver;
}

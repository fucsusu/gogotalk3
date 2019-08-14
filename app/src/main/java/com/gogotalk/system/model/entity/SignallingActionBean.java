package com.gogotalk.system.model.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fucc
 * Date: 2019-08-11 14:06
 */
public class SignallingActionBean {
    public int seq;
    public String action;
    public String role;
    public HashMap<String, String> data;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap data) {
        this.data = data;
    }
}

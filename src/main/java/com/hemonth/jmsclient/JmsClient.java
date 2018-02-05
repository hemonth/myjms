package com.hemonth.jmsclient;

public interface JmsClient {
    public void send(String msg);
    public String receive();
}

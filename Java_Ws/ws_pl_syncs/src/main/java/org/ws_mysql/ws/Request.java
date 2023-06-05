package org.ws_mysql.ws;

public class Request {
    String key;
    String type;
    String data;
    public Request(String key, String type, String data) {
        this.key = key;
        this.type = type;
        this.data = data;
    }


}

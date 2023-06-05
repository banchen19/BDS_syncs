package org.ws_mysql.ws;


import org.ws_mysql.ws.u.PlayerDatabase;

public class Webrtc_Server_Management {
    Webrtc_Server webrtcServer;
    static int Prot;

    public static String getKey() {
        return key;
    }

    public static void setKey(String keya) {
        key = keya;
    }

    static String key;
    static Message_Utils messageUtils;

    public Message_Utils getMessageUtils() {
        return messageUtils;
    }

    public static void setMessageUtils(Message_Utils messageUtilsa) {
        messageUtils = messageUtilsa;
    }

    public static void setProt(int prot) {
        Prot = prot;
    }

    private static Webrtc_Server_Management instance;
    GenericDatabase genericDatabase;
    PlayerDatabase playerDatabase;

    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    public static Webrtc_Server_Management getInstance()
    {
        if (instance == null) {
            instance = new Webrtc_Server_Management();
        }
        return instance;
    }
    Boolean isServer;

    public void setServer(Boolean server) {
        isServer = server;
    }

    Webrtc_Server_Management()
    {
        webrtcServer=new Webrtc_Server(Prot);
        setMessageUtils(new Message_Utils());
        webrtcServer.start();
        setServer(true);
        genericDatabase = new GenericDatabase<>();
        playerDatabase=new PlayerDatabase();
    }

    public GenericDatabase getGenericDatabase() {
        return genericDatabase;
    }

    public void setGenericDatabase(GenericDatabase genericDatabase) {
        this.genericDatabase = genericDatabase;
    }
}

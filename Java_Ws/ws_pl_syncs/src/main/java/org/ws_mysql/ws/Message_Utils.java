package org.ws_mysql.ws;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.ws_mysql.ws.u.Player;
import org.ws_mysql.ws.u.PlayerDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Message_Utils {
    static PlayerDatabase playerDatabase;
    static GenericDatabase genericDatabase;
    static List<WebSocket> webSocketList;
    static List<String> allPlayers;
    private static Player player;

    //接收json数据
    public static void Message(WebSocket webSocket, String ws_message) throws IOException, SQLException {
        playerDatabase = Webrtc_Server_Management.getInstance().getPlayerDatabase();
        genericDatabase = Webrtc_Server_Management.getInstance().getGenericDatabase();

        webSocketList = genericDatabase.getAll();
        allPlayers = playerDatabase.getAllPlayers();
        Request request = json_Request(ws_message);
        if (Objects.equals(request.key, Webrtc_Server_Management.getKey())) {
            if (!genericDatabase.contains(webSocket)) {
                genericDatabase.save(webSocket);
            }
            System.out.println("类型为: " + request.type);
            switch (request.type) {
                case "onJoin":
                    player = js_arr_toPlayer(request.data);
                    playerDatabase.addPlayer(player.name);
                    for (String pl_name : allPlayers) {
                        System.out.println("当前在线玩家：" + pl_name);
                        for (WebSocket webSocket1 : webSocketList) {
                            webSocket1.send(re_json(pl_name, "updata"));
                        }
                    }
                    System.out.println("玩家加入，当前服务器在线玩家人数：" + playerDatabase.getSize());
                    break;
                case "onLeft":
                    player = js_arr_toPlayer(request.data);
                    playerDatabase.removePlayer(player.name);
                    for (WebSocket webSocket1 : webSocketList) {
                        webSocket1.send(re_json(player.name, "left"));
                    }
                    System.out.println("玩家"+player.name+"退出，当前服务器在线玩家人数：" + playerDatabase.getSize());
                    break;
                case "chat":
                    System.out.println("来自：" + webSocket.getRemoteSocketAddress().toString() + "的消息：" + request.data);
                    List<WebSocket> webSocketList = genericDatabase.getAll();
                    for (WebSocket webSocket1 : webSocketList) {
                        webSocket1.send(re_json(request.data, "chat"));
                    }
                    break;
            }

        } else {
            webSocket.close();
        }
    }

    /**
     * 接收服务端的消息
     *
     * @param message 文本
     * @return Ser对象
     */
    static Request json_Request(String message) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        String key = jsonObject.get("key").getAsString();
        String type = jsonObject.get("type").getAsString();
        String data = jsonObject.get("data").getAsString();
        Request request = new Request(key, type, data);
        return request;
    }

    /**
     * @param value 解析玩家
     * @return 数据集
     */
    static Player js_arr_toPlayer(String value) {
        System.out.println(value);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(value).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        return new Player(name);
    }

    static String re_json(String text, String type) {
        JsonObject serverObject = new JsonObject();
        serverObject.addProperty("text", text);
        serverObject.addProperty("size", playerDatabase.getSize());
        serverObject.addProperty("key", Webrtc_Server_Management.getKey());
        serverObject.addProperty("type", type);
        return serverObject.toString();
    }
}

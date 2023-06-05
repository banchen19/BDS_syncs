package org.ws_mysql.ws.u;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDatabase {
    List<String> stringList=new ArrayList<>();

    public List<String> getAllPlayers() {
        return stringList;
    }

    public void addPlayer(String name) {
        stringList.add(name);
    }

    public void removePlayer(String name) {
        stringList.remove(name);
    }

    public int getSize() {
        return stringList.size();
    }
}


package org.ws_mysql.sql;

import java.sql.*;

public class Plmoney_SQL_Menage {

    static Connection conn;

    //玩家数据表创建语句
    public void creatTables() {
        String sql = "";
        int result = executeUpdate(sql);
        if (result == 0) {
            System.out.println("表创建成功");
        } else {
            System.out.println("表创建失败");
        }
    }
//玩家聊天记录表创建语句
public void creatTables_message() {
    String createTableSql = "CREATE TABLE players ("
            + "name VARCHAR(255) PRIMARY KEY,"
            + "state VARCHAR(255)"
            + ")";
    int result = executeUpdate(createTableSql);
    if (result == 0) {
        System.out.println("表创建成功");
    } else {
        System.out.println("表创建失败");
    }
}
    public static Connection connect(String ip, int port, String dataname, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dataname;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("已连接到MySQL数据库");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("MySQL数据库连接失败，请检查MySQL数据库是否已经开启，或检查数据库配置");
            e.printStackTrace();
        }
        return conn;
    }
    // 执行操作
    public int executeUpdate(String sql) {
        int result = 0;
        try {
            Statement stmt = conn.createStatement();
            System.out.println("执行操作");
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    //检测表是否存在
    public boolean isTableExists(Connection connection) {
        boolean exists = false;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "players", null);
            if (tables.next()) {
                exists = true;
            }
            tables.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

}

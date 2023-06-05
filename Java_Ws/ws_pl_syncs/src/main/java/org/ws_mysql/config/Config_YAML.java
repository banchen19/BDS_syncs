package org.ws_mysql.config;


import org.ws_mysql.sql.Plmoney_SQL_Menage;
import org.ws_mysql.sql.Plmoney_SQL_Util;
import org.ws_mysql.ws.Webrtc_Server_Management;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Config_YAML {

    public static String configFile="config.yml";
    /**
     * 创建yml配置文件
     */
    public static void creat_file_yaml() {
        // 创建数据映射
        Map<String, Object> data = new HashMap<>();
//        data.put("mysql", createMySQLConfig());
        data.put("ws", createMySQLConfig_ws());
        // 创建DumperOptions，用于配置输出格式
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置为块格式
        options.setPrettyFlow(true); // 设置为漂亮的格式（带缩进和换行）

        // 创建YAML实例并传递DumperOptions
        Yaml yaml = new Yaml(options);

        // 将数据写入YAML文件
        try (FileWriter writer = new FileWriter("config.yml")) {
            yaml.dump(data, writer);
            System.out.println("初次加载可能未生成文件，可重新启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> createMySQLConfig_ws() {
        Map<String, Object> config = new HashMap<>();
//        config.put("url", "jdbc:mysql://localhost:3306/mydatabase");
        config.put("port", 8887);
        config.put("key", generateRandomString());
        return config;
    }

    public static String generateRandomString() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        int STRING_LENGTH = 16;

        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private static Map<String, Object> createMySQLConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("ip", "localhost");
        config.put("port", 3306);
        config.put("user", "bc");
        config.put("password", "123456");
        config.put("mysql_db_name", "money");
        return config;
    }
    //读取
    public static void readConfigFile() {
        // 创建Yaml实例
        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            // 加载配置文件内容为Map
            Map<String, Object> data = yaml.load(fis);
            // 读取MySQL配置
            Map<String, Object> mysqlConfig = (Map<String, Object>) data.get("mysql");
            if (mysqlConfig != null) {
                String ip = (String) mysqlConfig.get("ip");
                int port = (int) mysqlConfig.get("port");
                String username = (String) mysqlConfig.get("user");
                String password = (String) mysqlConfig.get("password");
                String dataname = (String) mysqlConfig.get("mysql_db_name");

                System.out.println("获取config.yml配置成功");
                System.out.println("正在连接MySQL数据库");
                Plmoney_SQL_Util.setConnection(Plmoney_SQL_Menage.connect(ip,port,dataname, username, password));

            } else {
                System.out.println("未找到MySQL配置");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取ws配置——port
    public static int readConfigFile_port(File configFile) {
        int port = 0;
        Yaml yaml = new Yaml();
        try (FileInputStream fisa = new FileInputStream(configFile)) {
            // 加载配置文件内容为Map
            Map<String, Object> data = yaml.load(fisa);
            Map<String, Object> mysqlConfigws = (Map<String, Object>) data.get("ws");
            if (mysqlConfigws != null) {
                port = (int) mysqlConfigws.get("port");
                String key = (String) mysqlConfigws.get("key");
                Webrtc_Server_Management.setProt(port);
                Webrtc_Server_Management.setKey(key);
            } else {
                System.out.println("未找到MyWs配置");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return port;
    }
}

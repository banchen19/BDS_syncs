package org.ws_mysql;

import org.ws_mysql.config.Config_YAML;
import org.ws_mysql.sql.Plmoney_SQL_Menage;
import org.ws_mysql.sql.Plmoney_SQL_Util;
import org.ws_mysql.ws.Webrtc_Server_Management;

import java.io.File;

import static org.ws_mysql.config.Config_YAML.*;

public class Main {
    static File configFile_yml=new File(configFile);
    public static void main(String[] args) {
        if (configFile_yml.exists()) {
            //数据库连接
            conn();
        }else {
            creat_file_yaml();
            conn();
        }
    }
    private static void conn() {
        //阅读yml启动ws服务端
        int port= Config_YAML.readConfigFile_port(configFile_yml);
        if (port!=0) {
            Webrtc_Server_Management.getInstance();
//            System.out.println("检测MySQL数据库表");
//            readConfigFile();//同时完成启动连接数据库
//            Plmoney_SQL_Menage plmoneySqlMenage= Plmoney_SQL_Util.getInstance().getPlmoneySqlMenage();
//
//            if(!plmoneySqlMenage.isTableExists(Plmoney_SQL_Util.getConnection()))
//            {
//                System.out.println("玩家数据库不存在");
//                plmoneySqlMenage.creatTables_message();
//            }
//            System.out.println("ws服务端开启成功");
        } else {
            System.out.println("ws服务端开启失败");
        }
    }
}
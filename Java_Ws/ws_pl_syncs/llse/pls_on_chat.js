// LiteLoader-AIDS automatic generated
/// <reference path="e:\bds_api/dts/helperlib/src/index.d.ts"/> 


let Removelist = ll.import("ListHelper", "Removelist");
let Addlist = ll.import("ListHelper", "Addlist");
const wsc = new WSClient();
var path = ".\\plugins\\BC\\bc_mysql\\config_data.json";
var path_config = ".\\plugins\\BC\\bc_mysql\\config.json";

let config_data = {
    "ws://127.0.0.1:8887": {   //连接地址与端口
        key: "7m7T86VvwxHKfUfK"    //ws启动后出现的密钥，如果不知道，就这ws端启动后的config.yml内找到密钥复制过来
    },
}
var pl = null;
var tb_key;
var ws_uri;
var all_msg;
let config = {
    "name": "天台一号"
}
var CONFIG_int_ws = data.openConfig(path, "json", JSON.stringify(config_data));
var CONFIG = data.openConfig(path_config, "json", JSON.stringify(config));

function bc_log(s) {
    colorLog("green", "[同步系统]" + s);
}

//服务器启动
var WsArray = [];

mc.listen("onServerStarted", () => {
    bc_log("作者qq:738270846,邮箱: banchen8964@gmail.com");
    colorLog("red", "[同步系统:玩家人数、聊天]警告：由于崩服特性，本插件不支持热加载，请先关子服后关ws服务端");
    var fileContent = File.readFrom(path);
    var wsData = JSON.parse(fileContent);
    for (var ws_uri in wsData) {
        var ws_data = wsData[ws_uri];
        var wss = {
            ws: ws_uri,
            key: ws_data.key
        };
        WsArray.push(wss);
    }
    start_ser()

});

//连接请求
function connectWebSocket(ser_uri) {
    if (wsc.connect(ser_uri)) {
        colorLog("green", "[同步系统] 连接成功 " + ser_uri);
        return true;
    } else {
        colorLog("green", "[同步系统] 连接失败:  " + ser_uri);
        return false;
    }
}

//启动连接器
function start_ser() {
    var connected = false;
    while (!connected) {
        for (var i = 0; i < WsArray.length; i++) {
            if (connectWebSocket(WsArray[i].ws)) {
                //连接成功
                ws_uri = WsArray[i].ws;
                tb_key = WsArray[i].key;
                connected = true;
                break;
            } else {
                connected = false;
            }
        }
    }
}
wsc.listen("onError", (msg) => {
    log("发生错误: " + msg);
    log("请检测网络");
});

wsc.listen("onLostConnection", (code) => {
    log("连接丢失，错误码: " + code);
    log("请检测网络");
});
/**收到消息 */
wsc.listen("onTextReceived", (msg) => {
    const js_msg = JSON.parse(msg);
    if (js_msg.key === tb_key) {
        switch (js_msg.type) {
            case "updata":
                Removelist(js_msg.text)
                if (!up_data_pl(js_msg.text)) {
                    Addlist(js_msg.text, "0")
                    mc.setMotd(`§e服务器人数: §d${js_msg.size}`);
                }
                break;
            case "left":
                mc.setMotd(`§e服务器人数: §d${js_msg.size}`);
                Removelist(js_msg.text)
                break;
            case "chat":
                if (all_msg != js_msg.text) {
                    colorLog("green", "[同步系统]消息： " + js_msg.text);
                    mc.broadcast(js_msg.text)
                }
                break;
        }
    }

});
function up_data_pl(pl_name) {
    let pls = mc.getOnlinePlayers();
    return pls.some((pl, index) => {
        return pl_name === pl.realName;
    });

}

//玩家加入游戏
mc.listen("onChat", (play, msg) => {
    const name = CONFIG.get("name")
    all_msg = '[' + name + ']' + '<' + play.realName + "> " + msg
    sendQuery("chat", all_msg)
});
mc.listen("onJoin", (pl) => {
    up_Data("onJoin", pl)
})
mc.listen("onLeft", (pl) => {
    up_Data("onLeft", pl)
})
//初步查询判断插入
function up_Data(type, pl) {
    const data = {
        name: pl.realName,
    };
    sendQuery(type, JSON.stringify(data));
}

//发送设定
function sendQuery(type, data) {
    const json = {
        key: tb_key,
        type: type,
        data: data,
    };
    wsc.send(JSON.stringify(json));
}
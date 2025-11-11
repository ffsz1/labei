var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var F = require('./common/function');
var crypto = require('crypto')
    ,fs = require('fs');

// var heapdump = require('heapdump');

var C = require('./config');
var rpcClient = require('./rpcclient/rpcForSvr');

var WS_PORT = C.websocket_port;

var back_svr = _.str.sprintf("http://%s:%s", C.inner_host, WS_PORT+2);
var io = require('socket.io-client');


process.on('uncaughtException', function (err) {
  console.log(err);
  console.log(err.stack);
  F.addOtherLogs('proxy/proxy',["########err down:",err,err.stack]);
});

//加密
function cipher(buf ,cb){
    algorithm = "aes-128-cbc";
    var encrypted = "";
    var key = new Buffer(C.aes_key);
    var iv = new Buffer(C.aes_iv);
    var cip = crypto.createCipheriv(algorithm, key,iv);
    //cip.setAutoPadding(false);
   // buf = customPadding(buf);
    encrypted += cip.update(buf, 'utf8', 'base64');
    encrypted += cip.final('base64');
    cb(encrypted);
}
//解密
function decipher(encrypted,cb){
    algorithm = "aes-128-cbc";
    var decrypted = "";
    var key = new Buffer(C.aes_key);
    var iv = new Buffer(C.aes_iv);
    var decipher = crypto.createDecipheriv(algorithm, key, iv);
    //decipher.setAutoPadding(false);
    decrypted += decipher.update(encrypted, 'base64', 'utf8');
    decrypted += decipher.final('utf8');
    cb(decrypted);
}


// Websocket Server
var socketServer = new (require('ws').Server)({port: WS_PORT});
var connect_suc = 0;
socketServer.on('connection', (socket)=> {

    //F.addOtherLogs('proxydebug/proxydebug',[" socket connect:",socket.upgradeReq.url]);
    
    socket.up_time = new Date().getTime();

    var param = {"reconnection":false,"force new connection":true,"transports":['websocket']};

    var rpc_client = io.connect(back_svr, param);

    F.addOtherLogs('proxy/proxy', ["after connect socket num:",socketServer.clients.length]);

    // svr event
    socket.on('close', function(code, message){
        F.addOtherLogs('proxy/proxy', ["after close socket num:",socketServer.clients.length]);
        //F.log("debug","close reason: code:%s, message:%s",[code,message]);
            rpc_client.disconnect();
        rpc_client.removeAllListeners();
    });

    socket.on('error', function(error) {
    	console.log(error);
        F.addErrLogs(["proxy err:",error]);
            rpc_client.disconnect();
        rpc_client.removeAllListeners();
	});

    var emitToIMsvr = function(obj,str) {
        try {
            var route = obj['route'];
            if ("heartbeat" != route) {
                //F.addOtherLogs('proxy/proxy',["receive data:",str]);
            } else {
                //F.addOtherLogs('hb/hb',["receive data:",str]);
            }
            obj.is_websocket = 1;
            rpc_client.emit("all#route", obj);
        } catch(err) {
            F.addOtherLogs('proxy/proxy',[" emit err:"+err.stack,obj]);
        }
    }


    socket.on("message", function(str) {
    	socket.up_time = new Date().getTime();
        try {
            var obj = JSON.parse (str);
            if ("ed" in obj) {
	        socket.ed = true;
	        let edata = obj["ed"]; 
		decipher(edata,function(decode_str) {
		    try {
                        var decode_obj = JSON.parse (decode_str);
			emitToIMsvr(decode_obj,decode_str);
		    } catch(e) {
                        F.addOtherLogs('proxy/proxy',["decipher parse err:"+e.stack,str]);
		    }
		});
	    } else {
	        emitToIMsvr(obj,str);
	    }
        } catch(err) {
            F.addOtherLogs('proxy/proxy',[" parse err:"+err.stack,str]);
        }
    });


    // client event
    rpc_client.on('connect', function() {
        ++connect_suc;
        console.log("client socket "+connect_suc+" connect suc");
    });

    rpc_client.on('disconnect', function() {
        console.log("client socket disconnect");
        //delete rpc_client.io;
        socket.close();
    });

    rpc_client.on('connect_error', function() {
        //console.log("client socket connect err");
        //delete rpc_client.io;
        socket.close();
    });

    rpc_client.on('connect_timeout', function() {
        //console.log("client socket connect_timeout");
        //delete rpc_client.io;
        socket.close();
    });

    var sendData = function(send_data) {
        try {
            socket.send(send_data);
        }
        catch(e) {
            F.addOtherLogs('proxy/proxy',["send proxy err:",e.stack,send_data]);
            try {
                socket.close();
            }
            catch(e2) {
                F.addOtherLogs('proxy/proxy',["close socket err",e2.stack]);
            }
        }
    }

    rpc_client.on('*', function(data) {
    	socket.up_time = new Date().getTime();
        var send_data = data;
        if (typeof(data) == "object") {
            send_data = JSON.stringify(data);
            if ('heartbeat' == data.route) {
                //F.addOtherLogs('hb/hb',["send data:",send_data]);
            } else {
                //F.addOtherLogs('proxy/proxy',["send data:",send_data]);
            }
        }
	if (F.isNull(socket.ed)) {
            sendData(send_data);
	} else {
            try {
	        cipher(send_data,function(encode_str) {
	            let send_data = {"ed":encode_str}
                    //F.addOtherLogs('proxy/proxy',["cipher data:",JSON.stringify(send_data)]);
		    sendData(JSON.stringify(send_data));
	        });
	    } catch(e) {
                F.addOtherLogs('proxy/proxy',["cipher err:",send_data]);
	    }
	}


    });


});

//var memwatch = require('memwatch');
//
//memwatch.on('leak', function(info) {
// console.error('Memory leak detected: ', info);
//});



//setInterval(function(){
//    console.log("Before:",process.memoryUsage());
//    gc();
//    console.log("After:",process.memoryUsage());
//},5000);

//setInterval(function() {
//    for (var i=0;i<socketServer.clients.length;i++) {
//        var cur_time = new Date().getTime();
//        //console.log("curtime:",cur_time);
//        //console.log("connect_time:", socketServer.clients[i].connect_time);
//        if (cur_time > socketServer.clients[i].up_time + 60*1000) socketServer.clients[i].close();
//    }
//}, 10000);

console.log('Listening on PORT: '+WS_PORT);
F.addOtherLogs('proxy/proxy',['Listening on PORT: '+WS_PORT]);

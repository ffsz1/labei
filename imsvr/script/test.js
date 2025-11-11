var crypto = require('crypto')
    ,fs = require('fs');


function customPadding(str) {
  str = new Buffer(str,"utf8").toString("hex");
  var bitLength = str.length*8;

  if(bitLength < 256) {
    for(i=bitLength;i<256;i+=8) {
      str += 0x0;
    }
  } else if(bitLength > 256) {
    while((str.length*8)%256 != 0) {
      str+= 0x0;
    }
  }
  return new Buffer(str,"hex").toString("utf8");
}



//加密
function cipher(algorithm, key, buf ,cb){
    var encrypted = "";
    var key = new Buffer(key);
    iv = new Buffer(0);
    var cip = crypto.createCipheriv(algorithm, key,iv);
    //cip.setAutoPadding(false);
   // buf = customPadding(buf);
    encrypted += cip.update(buf, 'utf8', 'base64');
    encrypted += cip.final('base64');
    cb(encrypted);
}
//解密
function decipher(algorithm, key, encrypted,cb){
    var decrypted = "";
    var key = new Buffer(key);
    iv = new Buffer(0);
    var decipher = crypto.createDecipheriv(algorithm, key, iv);
    //decipher.setAutoPadding(false);
    decrypted += decipher.update(encrypted, 'base64', 'utf8');
    decrypted += decipher.final('utf8');
    cb(decrypted);
}
function cipherDecipherFile(data,algorithm, key){
        var s1 = new Date();
        cipher(algorithm, key,data,function(encrypted){
            var s2 = new Date();
            //console.log('cipher:'+algorithm+','+(s2-s1) +'ms, '+encrypted);
	    //console.log(encrypted);
	    //encrypted = "FJwlN9EDjrCbYgLYf91q1fR4mEFOJVLgUSbSebDTCk0stRV4X7xw3l1hyjS4Xe7qVzV13h0mgMSm+CjN/VPl1klRpcPSYhGPymeCx+mNofsLOKu+Myja9A=="
            decipher(algorithm, key,encrypted,function(txt){
                var s3 = new Date();
                //console.log('decipher:'+algorithm+','+(s3-s2) +'ms, '+txt);
		//console.log("原文:",txt,"des:",encrypted,"\n");
            });
        });
}
//console.log(crypto.getCiphers());
var algs = [
  'des-ecb'
];
var key="miaomiao";
var filename = "./b.txt";//"package.json";
algs.forEach(function(name){

    var data_list = [
    {"aavv":new Date().getTime(), "ddsfds ":"sdfjk*===%sdfkjlk"},
    {"id":2343,"route":"im/login","data":{"a":"ddddddd"}},
    {"key= &&yy":{"aavv":new Date().getTime(), "ddsfds ":"sdfjk*===%sdfkjlk"}},
    {"key= &&yy":{"aa=你好=34&vv":new Date().getTime(), "dds234^&%fds ":"sdfjk*===%sdfkjlk"}},
    {"key= &&yy":{"aa=ʚ🌝ɞ阿lu=34&vv":new Date().getTime(), "dds234^&%fds ":{"ds7^*&":33333}}},
    ];

    let s = new Date().getTime();
    for (let j = 0; j < 1000;j++) {
        for (let i = 0; i < data_list.length; i++) {
            cipherDecipherFile(JSON.stringify(data_list[i]),name,key);
        }
    }
    let e = new Date().getTime();
    console.log(e - s);
 
})


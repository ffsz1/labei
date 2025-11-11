'use strict';
var fs=require('fs');
var F = require('../common/function');
var path = require('path');

function modelMgr() {

	var model_path = path.resolve(__dirname, '../model/');
	this.model_map = {};
	var that = this;

	this.init = function (){
		try{
			if(!fs.existsSync(model_path)) {
				F.throwErr("model path err");
			}
			var files = fs.readdirSync(model_path);
            console.log(files);
			files.forEach(function(file, index) {
                try {
				    if(file != 'model_base.js' && file.endsWith(".js")){
				    	var class_name = file.substr(0, file.length - 3);
				    	var model_obj = require('../model/' + class_name);
				    	that.model_map[class_name] = new model_obj(class_name,that);
				    }
                } catch(e) {
                	F.log("loading model err:"+file);
                    F.log(e);
                    console.log(e);
                }
			});
		}catch(e) {
			console.log(e);
		}
			
	}

	this.init();

}


module.exports = modelMgr;

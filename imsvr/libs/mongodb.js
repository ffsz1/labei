/**
 * example:
 var mongo = require('../libs/mongodb');
 exports.userModel = mongo.user;
 exports.poiModel = mongo.poi;
 mongo.initDB();

 var userInfo = new exports.userModel;
 userInfo._id = 'art_userid_'+user.uid;
 userInfo.timeline=this.timestamp();
 userInfo.loc = [user.longitude,user.latitude];
 userInfo.save();
 */

'use strict';

/**
 * Module dependencies.
 */

var mongoose     = require('mongoose');
var Schema     = mongoose.Schema;

var C = require('../config');


//var ynDB = mongoose.connection;

var poiSchema = new Schema({
  _id: String,
  poiName : String,
  tagid: Number,
  loc : {
    type: [Number],
    index: '2d'
  }
});

var userSchema = new Schema({
  _id: String,
  timeline:Number,
  loc : {
    type: [Number],
    index: '2d'
  }
});

exports.poi = mongoose.model('poi',poiSchema);
exports.user = mongoose.model('user',userSchema);
exports.initDB = function(){
  mongoose.connect(C.mongo.uri, C.mongo.options);
};
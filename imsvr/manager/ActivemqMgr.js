/**
 * 直播间男女端共用Manager
 */
'use strict';
const F = require('../common/function');
const C = require('../config');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
const co = require('co');
var Stomp = require('stomp-client');

module.exports = function (app, commonManager) {
    let mgr_map = commonManager.mgr_map;
    let that = this;
    //this.client = new Stomp('172.17.13.235', 61613);
    //this.client = new Stomp('39.105.187.28', 61613);
    //this.client = new Stomp({
    //  address: '39.105.187.28',
    //  port: 61613,
    //  user: '',
    //  pass: '', 
    //  protocolVersion: '1.1', 
    //  vhost: null, 
    //  reconnectOpts: { retries: 100000000, delay: 500 }});
    //this.sessId = null;

    //this.client.connect(function(sessionId) {
    //    that.sessId = sessionId;
    //    that.client.subscribe("/queue/test_delay_queue",function(body, headers) {
    //        F.addDebugLogs(["/queue/test_delay_queue recv: pre"]);
    //        F.addDebugLogs(["/queue/test_delay_queue recv:",body,headers]);
    //    });
    //    //client.subscribe(destination, function(body, headers) {
    //    //    console.log('From MQ:', body);
    //    //});

    //    //client.publish(destination, 'Hello World!');
    //});


    //var deasync = require('deasync');
    //while (true) {
    //    if (null != this.sessId) break;
    //    console.log("waiting for active connect.");
    //    deasync.sleep(200);
    //}
    //console.log("active connect suc.");

};


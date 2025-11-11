package com.erban.main.service;

import com.erban.main.service.wx.WxService;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 北岭山下 on 2017/7/24.
 */
/*
                微信订单查询


                <xml>
                <appid>wx2421b1c4370ec43b</appid>
                <mch_id>10000100</mch_id>
                <nonce_str>ec2316275641faa3aacf3cc599e8730f</nonce_str>
                <transaction_id>1008450740201411110005820873</transaction_id>
                <sign>FDD167FAA73459FD921B144BAF4F4CA2</sign>
                </xml>


 */
@Service
public class WXOrderQueryService {



        @Autowired
        WxService wxService;


        /**
         *      查询订单结果
         * @param outTradeNo 商品订单号
         * @return      订单查询结果
         */
        public BusiResult getWXOrder(String outTradeNo){

/*
                Map<String,Object> map = new HashMap <> (  );
                map.put ( "appid" ,appid);
                map.put ( "mch_id", );
                map.put ( "nonce_str", );
                map.put ( "transaction_id", );
                map.put ( "sign", );*/

                return null;







        }




}

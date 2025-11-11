package com.erban.main.util;


import com.erban.main.config.WxConfig;
import com.erban.main.wechat.AccessToken;
import com.erban.main.wechat.MyX509TrustManager;
import com.erban.main.wechat.pojo.Menu;
import com.google.gson.*;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.utils.PropertyUtil;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * 微信公众接口工具类
 *
 * 项目名称：WeiChatService
 */
public class WeiXinUtil {

        private static final Logger LOGGER = Logger.getLogger ( WeiXinUtil.class );

        // 获取access_token的接口地址（GET） 限200（次/天）
        public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        // 菜单创建（POST） 限100（次/天）
        public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        //发送模板消息
        public static String model_msg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

        /**
         * 发起https请求
         * @param requestUrl 请求地址
         * @param requestMethod 请求方式（Get或者post）
         * @param outputStr 提交数据
         * @return
         */
        public static JsonObject httpsRequest ( String requestUrl, String requestMethod, String outputStr ) {
                JsonObject jsonObject = null;
                StringBuffer buffer = new StringBuffer ( );
                try {
                        //创建SSLcontext管理器对像，使用我们指定的信任管理器初始化
                        TrustManager[] tm = { new MyX509TrustManager ( ) };
                        SSLContext sslContext = SSLContext.getInstance ( "SSL", "SunJSSE" );
                        sslContext.init ( null, tm, new java.security.SecureRandom ( ) );
                        SSLSocketFactory ssf = sslContext.getSocketFactory ( );

                        URL url = new URL ( requestUrl );
                        HttpsURLConnection httpsUrlConn = ( HttpsURLConnection ) url.openConnection ( );
                        httpsUrlConn.setSSLSocketFactory ( ssf );
                        httpsUrlConn.setDoInput ( true );
                        httpsUrlConn.setDoOutput ( true );
                        httpsUrlConn.setUseCaches ( false );
                        //设置请求方式（GET/POST）
                        httpsUrlConn.setRequestMethod ( requestMethod );
                        if ( "GET".equalsIgnoreCase ( requestMethod ) ) {
                                httpsUrlConn.connect ( );
                        }

                        //当有数据需要提交时
                        if ( outputStr != null ) {
                                OutputStream outputStream = httpsUrlConn.getOutputStream ( );
                                //防止中文乱码
                                outputStream.write ( outputStr.getBytes ( "UTF-8" ) );
                                outputStream.close ( );
                                outputStream = null;
                        }

                        //将返回的输入流转换成字符串
                        InputStream inputStream = httpsUrlConn.getInputStream ( );
                        InputStreamReader inputStreamReader = new InputStreamReader ( inputStream, "UTF-8" );
                        BufferedReader bufferedReader = new BufferedReader ( inputStreamReader );

                        String str = null;
                        while ( ( str = bufferedReader.readLine ( ) ) != null ) {
                                buffer.append ( str );
                        }

                        bufferedReader.close ( );
                        inputStreamReader.close ( );

                        inputStream.close ( );
                        inputStream = null;

                        httpsUrlConn.disconnect ( );
//                        jsonObject = JSONObject.fromObject ( buffer.toString ( ) );
                        JsonElement jsonElement = new JsonParser ().parse ( buffer.toString () );
                        jsonObject = jsonElement.getAsJsonObject ();
                        LOGGER.info (jsonObject );

                } catch ( ConnectException ce ) {
                        // TODO: handle exception
                        LOGGER.error ( "Weixin server connection timed out." );
                } catch ( Exception e ) {
                        // TODO: handle exception
                        LOGGER.error ( "https request error:{}", e );
                }

                return jsonObject;
        }

        public static AccessToken getAccessToken ( String appid, String appsecret ) {
                AccessToken accessToken = null;
                String requestUrl = access_token_url.replace ( "APPID", appid ).replace ( "APPSECRET", appsecret );
                LOGGER.info ("=========获取AccessToken========");
                LOGGER.info ( requestUrl );
                JsonObject jsonObject = httpsRequest ( requestUrl, "GET", null );
                //如果请求成功
                if ( jsonObject != null ) {
                        try {
                                accessToken = new AccessToken ( );
                                accessToken.setToken ( jsonObject.get ( "access_token" ).toString ().replace ( "\"","" ) );
                                accessToken.setExpiresIn ( jsonObject.get ( "expires_in" ).toString ().replace ( "\"","" ) );
                        } catch ( Exception e ) {
                                // TODO: handle exception
                                // TODO: handle exception
                                accessToken = null;
                                // 获取token失败
                                LOGGER.info ( "获取token失败 errcode:{" + jsonObject.get ( "errcode" ) + "} errmsg:{" + jsonObject.get ( "errmsg" ) + "}" );

                        }
                }
                return accessToken;
        }
        public static void main(String [] a){
                String appid="wx009d793f92c24eec";
                String appsecret = "d99ac5ed29071943d1654a40bb0adb68";
                AccessToken accessToken = null;
                String requestUrl = access_token_url.replace ( "APPID", appid ).replace ( "APPSECRET", appsecret );
                System.out.println ("===="+requestUrl );
                accessToken = getAccessToken (appid,appsecret  );
                System.out.println (accessToken.getToken () );
                System.out.println (accessToken.getExpiresIn () );
        }

        /**
         * 创建菜单
         *
         * @param menu 菜单实例
         * @param accessToken 有效的access_token
         * @return 0表示成功，其他值表示失败
         */
        public static String createMenu ( Menu menu, String accessToken ) {
                String  result = "";

                // 拼装创建菜单的url
                String url = menu_create_url.replace ( "ACCESS_TOKEN", accessToken );
                // 将菜单对象转换成json字符串
//                String jsonMenu = JSONObject.fromObject ( menu ).toString ( );
                LOGGER.info ( "==========url:"+url );
                Gson gson = new GsonBuilder ().disableHtmlEscaping().create();
                String jsonMenu = gson.toJson (menu);
                LOGGER.info ( "自定义菜单json：" + jsonMenu );
                // 调用接口创建菜单
                JsonObject jsonObject = httpsRequest ( url, "POST", jsonMenu );
                if ( null != jsonObject ) {
                        if ( !jsonObject.get ( "errcode" ).equals ( "0" ) ) {
                                result = jsonObject.get ( "errcode" ).toString ();
                        }
                }

                return result;
        }
        public static void sendModelMsg ( Long erbanNo,String  openId,String goldNum, String accessToken,String resultStr ) {

                String url = model_msg_url.replace ( "ACCESS_TOKEN", accessToken );
                //TODO 测试,冲多少都只要一分钱
                String jsonModelMsg = "{ \n" +
                        "    \"touser\":\""+openId+"\", \n" +
                        "    \"template_id\":\""+ WxConfig.modelMsgId +"\", \n" +
                        "    \"topcolor\":\"#FF0000\", \n" +
                        "    \"data\":{ \n" +
                        "            \"first\": {\n" +
                        "\n" +
                        "                \"value\":\""+ GlobalConfig.appName + "语音支付订单"+"\",\n" +
                        "\n" +
                        "                \"color\":\"#173177\"\n" +
                        "\n" +
                        "            },\n" +
                        "            \"accountType\": {\n" +
                        "\n" +
                        "                \"value\":\"" + GlobalConfig.appName + "号"+"\",\n" +
                        "\n" +
                        "                \"color\":\"#173177\"\n" +
                        "\n" +
                        "            },\n" +
                        "            \"account\": {\n" +
                        "\n" +
                        "                \"value\":\""+erbanNo+""+"\",\n" +
                        "\n" +
                        "                \"color\":\"#173177\"\n" +
                        "\n" +
                        "            },\n" +
                        "            \"amount\": {\n" +
                        "\n" +
                        "                \"value\":\""+goldNum+" 金币"+"\",\n" +
                        "\n" +
                        "                \"color\":\"#173177\"\n" +
                        "\n" +
                        "            },\n" +
                        "\n" +
                        "            \"result\":{\n" +
                        "\n" +
                        "                \"value\":\""+resultStr+"\",\n" +
                        "\n" +
                        "                \"color\":\"#173177\"\n" +
                        "\n" +
                        "            },\n" +
                        "\n" +
                        "            \"remark\":{ \n" +
                        "                \"value\":\"备注：如有疑问，请致电客服。\", \n" +
                        "                \"color\":\"颜色#173177\" \n" +
                        "            } \n" +
                        "    } \n" +
                        " }";
//                LOGGER.info ( "模板消息json：" + jsonModelMsg );
                JsonObject jsonObject = httpsRequest ( url, "POST", jsonModelMsg );
                //{"errcode":0,"errmsg":"ok","msgid":467018897}
                if ( null != jsonObject ) {
                        if ( !jsonObject.get ( "errcode" ).toString ().equals ( "0" ) ) {
                                LOGGER.info ( "发送充值通知失败  errcode:{" + jsonObject.get ( "errcode" ) + "} errmsg:{" + jsonObject.get ( "errmsg" ) + "}" );
                        }else{
                                LOGGER.info ( "发送充值通知成功！" + GlobalConfig.appName + "号："+erbanNo);
                        }
                }
        }
}

package com.juxiao.xchat.base.utils;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

public class WebAppUtils {

    /**
     * 获取服务器真实IP
     *
     * @return
     * @throws IOException
     */
    public static String getIP() throws IOException {
        URL url = new URL("http://2018.ip138.com/ic.asp");
        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        try (InputStream ins = con.getInputStream()) {
            InputStreamReader isReader = new InputStreamReader(ins, "UTF8");
            BufferedReader bReader = new BufferedReader(isReader);
            StringBuilder content = new StringBuilder();
            String str;
            while ((str = bReader.readLine()) != null) {
                content.append(str);
            }

            int start = content.indexOf("[") + 1;
            int end = content.indexOf("]");
            return content.substring(start, end);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取tomcat服务器端口
     * @return
     * @throws MalformedObjectNameException
     * @throws NullPointerException
     */
    public static String getPort() throws MalformedObjectNameException, NullPointerException{
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        String port = "";
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        if(objectNames.size() > 0) {
            port = objectNames.iterator().next().getKeyProperty("port");
        }
        return String.valueOf(port);
    }


}

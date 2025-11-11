package com.juxiao.xchat.api.controller.test;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;


@RestController
@RequestMapping("/httptest")
public class TestController {

    private int c = 0;

    public String execShell(String scriptPath, String ... para) {
        try {
            String[] cmd = new String[]{scriptPath};
            //为了解决参数中包含空格
            cmd = (String[]) ArrayUtils.addAll(cmd,para);

            //解决脚本没有执行权限
            ProcessBuilder builder = new ProcessBuilder("chmod", "755", scriptPath);
            Process process = builder.start();
            process.waitFor();

            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            //执行结果
            String result = sb.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "";
        }
    }

    public String execCmd(String cmd) {
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            //执行结果
            String result = sb.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "v2/test", method = RequestMethod.GET)
    public WebServiceMessage testv1(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        System.out.println("----------------------c:" + c);
//        c++;
////        Thread.sleep(10 * 1000L);
//        if(c % 2 == 1){
//            response.sendError(500);
//            WebServiceMessage.success("api error");
//        }
        return WebServiceMessage.success("api echo");
    }

    @RequestMapping(value = "v2/test2", method = RequestMethod.GET)
    public WebServiceMessage testv2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return WebServiceMessage.success(this.execCmd("cat /data/java/webapps/appservice/start-appservice.sh"));
    }
}
package com.erban.admin.web.back;

import com.erban.main.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/controller")
public class CentreController {

    @RequestMapping(value = "/skip",method = RequestMethod.GET)
    public String skip(String address) {
        if (StringUtils.isEmpty(address)) {
            return "error";
        }
        return address;
    }
}

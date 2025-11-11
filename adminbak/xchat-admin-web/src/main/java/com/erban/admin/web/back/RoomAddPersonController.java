package com.erban.admin.web.back;


import com.erban.admin.main.service.RoomAddPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
public class RoomAddPersonController {
    @Autowired
    private RoomAddPersonService roomAddPersonService;

    @RequestMapping("/addperson")
    @ResponseBody
    public String addPerson(Long erbanNo, Long personNum, Byte gender) throws Exception {
        if (erbanNo == null || personNum == null) {
            return null;
        }
        roomAddPersonService.addPerson(erbanNo, personNum, gender);
        return "success";
    }
}

package com.erban.admin.web.controller.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.model.RoomSensitiveWords;
import com.erban.admin.main.service.room.RoomSensitiveWordsService;
import com.erban.admin.main.service.room.enumeration.SensitiveWordEnum;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.dto.SensitiveDTO;
import com.erban.admin.web.util.ExcelUtils;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/room/sensitive")
public class RoomSensitiveWordsController extends BaseController {
    @Autowired
    private RoomSensitiveWordsService roomSensitiveWordsService;

    /**
     * 查询用户列表
     *
     * @param searchText 用户名称
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getList(String searchText, Integer type) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<RoomSensitiveWords> pageInfo = roomSensitiveWordsService.getList(getPageNumber(), getPageSize(),
                searchText, type);
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 保存/更新
     *
     * @param sensitiveWords
     */
    @RequestMapping("/save")
    @ResponseBody
    public void save(RoomSensitiveWords sensitiveWords) {
        int result = -1;
        if (sensitiveWords != null) {
            try {
                sensitiveWords.setAdminId(getAdminId());
                result = roomSensitiveWordsService.saveRoomSensitiveWords(sensitiveWords);
            } catch (Exception e) {
                logger.warn("saveRoomSensitiveWords fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/del")
    @ResponseBody
    public void del(Integer id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                roomSensitiveWordsService.deleteRoomSensitiveWords(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                logger.warn("deleteRoomSensitiveWords fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        File file = ResourceUtils.getFile("classpath:template/sensitive.xlsx");
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("敏感词模版" + ".xlsx").getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }

    @RequestMapping("/importExcel")
    @ResponseBody
    public void importExcel(@RequestParam("file") MultipartFile uploadFile) {
        int result = -1;
        if (!uploadFile.isEmpty()) {
            List<SensitiveDTO> importExcel = ExcelUtils.importExcel(uploadFile, 0, 1, SensitiveDTO.class);
            List<RoomSensitiveWords> roomSensitiveWordsList = new ArrayList<>();
            List<Integer> types = Arrays.asList(1, 2, 3, 4);
            assert importExcel != null;
            importExcel.forEach(item -> {
                types.forEach(itemType -> {
                    RoomSensitiveWords roomSensitiveWords = new RoomSensitiveWords();
                    byte type = (byte) itemType.intValue();
                    roomSensitiveWords.setAdminId(getAdminId());
                    roomSensitiveWords.setCreateTime(new Date());
                    roomSensitiveWords.setType(type);
                    roomSensitiveWords.setSensitiveWords(item.getSensitiveWord());
                    roomSensitiveWordsList.add(roomSensitiveWords);
                });

            });
            result = roomSensitiveWordsService.batchSave(roomSensitiveWordsList);
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }
}

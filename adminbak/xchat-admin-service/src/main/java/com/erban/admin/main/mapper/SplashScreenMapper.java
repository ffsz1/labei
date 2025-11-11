package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.SplashScreenDTO;
import com.erban.main.model.ActivityHtml;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SplashScreenMapper {
    List<SplashScreenDTO> queryByCondition(@Param("type") Integer type, @Param("status") Integer status, @Param("userType") Integer userType);

    SplashScreenDTO queryByID(@Param("id") Integer id);

    int insertSplashScreen(SplashScreenDTO splashScreenDTO);

    int updateSplashScreen(SplashScreenDTO splashScreenDTO);
}

package com.erban.main.mybatismapper;

import com.erban.main.model.UserGuessNameRecord;
import com.erban.main.model.vo.GuessNameRecordDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserGuessNameRecordMapper {

    @Insert("INSERT INTO user_guess_name_record (uid, guess_name, create_date) VALUES (#{uid}, #{guessName}, #{createDate})")
    int insert(UserGuessNameRecord record);

    @Select("SELECT a.id, a.uid, a.guess_name as guessName, a.create_date as createDate, u.erban_no as erbanNo, u.nick FROM user_guess_name_record a INNER JOIN users u ON a.uid = u.uid ORDER BY a.id LIMIT #{pageNum}, 50")
    List<GuessNameRecordDTO> listAll(Integer pageNum);
}

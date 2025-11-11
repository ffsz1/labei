package com.erban.main.mybatismapper;

import com.erban.main.model.OrderServ;
import com.erban.main.model.OrderServExample;
import java.util.List;

public interface OrderServMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(OrderServ record);

    int insertSelective(OrderServ record);

    List<OrderServ> selectByExample(OrderServExample example);

    OrderServ selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrderServ record);

    int updateByPrimaryKey(OrderServ record);
}

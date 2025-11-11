package com.erban.main.mybatismapper;

import com.erban.main.model.WithDrawCashProd;
import com.erban.main.model.WithDrawCashProdExample;
import java.util.List;

public interface WithDrawCashProdMapper {
	int deleteByPrimaryKey(String cashProdId);

	int insert(WithDrawCashProd record);

	int insertSelective(WithDrawCashProd record);

	List<WithDrawCashProd> selectByExample(WithDrawCashProdExample example);

	WithDrawCashProd selectByPrimaryKey(String cashProdId);

	int updateByPrimaryKeySelective(WithDrawCashProd record);

	int updateByPrimaryKey(WithDrawCashProd record);

	List<WithDrawCashProd> selectAll();
}

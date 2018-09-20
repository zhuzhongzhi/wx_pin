/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.mapper.manytoone;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.entity.manytoone.Goods;

/**
 * 商品MAPPER接口
 * 
 * @author liugf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
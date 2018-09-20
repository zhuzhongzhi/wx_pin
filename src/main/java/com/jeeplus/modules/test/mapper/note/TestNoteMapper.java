/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.mapper.note;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.entity.note.TestNote;

/**
 * 富文本测试MAPPER接口
 * 
 * @author liugf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface TestNoteMapper extends BaseMapper<TestNote> {

}
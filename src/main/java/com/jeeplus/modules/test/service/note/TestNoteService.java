/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.note;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.entity.note.TestNote;
import com.jeeplus.modules.test.mapper.note.TestNoteMapper;

/**
 * 富文本测试Service
 * 
 * @author liugf
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class TestNoteService extends CrudService<TestNoteMapper, TestNote> {

	public TestNote get(String id) {
		return super.get(id);
	}

	public List<TestNote> findList(TestNote testNote) {
		return super.findList(testNote);
	}

	public Page<TestNote> findPage(Page<TestNote> page, TestNote testNote) {
		return super.findPage(page, testNote);
	}

	@Transactional(readOnly = false)
	public void save(TestNote testNote) {
		super.save(testNote);
	}

	@Transactional(readOnly = false)
	public void delete(TestNote testNote) {
		super.delete(testNote);
	}

}
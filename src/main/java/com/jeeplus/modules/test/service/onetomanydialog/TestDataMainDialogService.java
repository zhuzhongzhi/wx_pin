/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.onetomanydialog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.entity.onetomanydialog.TestDataMainDialog;
import com.jeeplus.modules.test.mapper.onetomanydialog.TestDataMainDialogMapper;
import com.jeeplus.modules.test.entity.onetomanydialog.TestDataChild11;
import com.jeeplus.modules.test.mapper.onetomanydialog.TestDataChild11Mapper;
import com.jeeplus.modules.test.entity.onetomanydialog.TestDataChild22;
import com.jeeplus.modules.test.mapper.onetomanydialog.TestDataChild22Mapper;
import com.jeeplus.modules.test.entity.onetomanydialog.TestDataChild33;
import com.jeeplus.modules.test.mapper.onetomanydialog.TestDataChild33Mapper;

/**
 * 票务代理Service
 * 
 * @author liugf
 * @version 2017-06-11
 */
@Service
@Transactional(readOnly = true)
public class TestDataMainDialogService extends CrudService<TestDataMainDialogMapper, TestDataMainDialog> {

	@Autowired
	private TestDataChild11Mapper testDataChild11Mapper;
	@Autowired
	private TestDataChild22Mapper testDataChild22Mapper;
	@Autowired
	private TestDataChild33Mapper testDataChild33Mapper;

	public TestDataMainDialog get(String id) {
		TestDataMainDialog testDataMainDialog = super.get(id);
		testDataMainDialog
				.setTestDataChild11List(testDataChild11Mapper.findList(new TestDataChild11(testDataMainDialog)));
		testDataMainDialog
				.setTestDataChild22List(testDataChild22Mapper.findList(new TestDataChild22(testDataMainDialog)));
		testDataMainDialog
				.setTestDataChild33List(testDataChild33Mapper.findList(new TestDataChild33(testDataMainDialog)));
		return testDataMainDialog;
	}

	public List<TestDataMainDialog> findList(TestDataMainDialog testDataMainDialog) {
		return super.findList(testDataMainDialog);
	}

	public Page<TestDataMainDialog> findPage(Page<TestDataMainDialog> page, TestDataMainDialog testDataMainDialog) {
		return super.findPage(page, testDataMainDialog);
	}

	@Transactional(readOnly = false)
	public void save(TestDataMainDialog testDataMainDialog) {
		super.save(testDataMainDialog);
		for (TestDataChild11 testDataChild11 : testDataMainDialog.getTestDataChild11List()) {
			if (testDataChild11.getId() == null) {
				continue;
			}
			if (TestDataChild11.DEL_FLAG_NORMAL.equals(testDataChild11.getDelFlag())) {
				if (StringUtils.isBlank(testDataChild11.getId())) {
					testDataChild11.setTestDataMain(testDataMainDialog);
					testDataChild11.preInsert();
					testDataChild11Mapper.insert(testDataChild11);
				} else {
					testDataChild11.preUpdate();
					testDataChild11Mapper.update(testDataChild11);
				}
			} else {
				testDataChild11Mapper.delete(testDataChild11);
			}
		}
		for (TestDataChild22 testDataChild22 : testDataMainDialog.getTestDataChild22List()) {
			if (testDataChild22.getId() == null) {
				continue;
			}
			if (TestDataChild22.DEL_FLAG_NORMAL.equals(testDataChild22.getDelFlag())) {
				if (StringUtils.isBlank(testDataChild22.getId())) {
					testDataChild22.setTestDataMain(testDataMainDialog);
					testDataChild22.preInsert();
					testDataChild22Mapper.insert(testDataChild22);
				} else {
					testDataChild22.preUpdate();
					testDataChild22Mapper.update(testDataChild22);
				}
			} else {
				testDataChild22Mapper.delete(testDataChild22);
			}
		}
		for (TestDataChild33 testDataChild33 : testDataMainDialog.getTestDataChild33List()) {
			if (testDataChild33.getId() == null) {
				continue;
			}
			if (TestDataChild33.DEL_FLAG_NORMAL.equals(testDataChild33.getDelFlag())) {
				if (StringUtils.isBlank(testDataChild33.getId())) {
					testDataChild33.setTestDataMain(testDataMainDialog);
					testDataChild33.preInsert();
					testDataChild33Mapper.insert(testDataChild33);
				} else {
					testDataChild33.preUpdate();
					testDataChild33Mapper.update(testDataChild33);
				}
			} else {
				testDataChild33Mapper.delete(testDataChild33);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(TestDataMainDialog testDataMainDialog) {
		super.delete(testDataMainDialog);
		testDataChild11Mapper.delete(new TestDataChild11(testDataMainDialog));
		testDataChild22Mapper.delete(new TestDataChild22(testDataMainDialog));
		testDataChild33Mapper.delete(new TestDataChild33(testDataMainDialog));
	}

}
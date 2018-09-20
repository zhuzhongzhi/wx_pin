/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.treetable;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.entity.treetable.Car;
import com.jeeplus.modules.test.mapper.treetable.CarMapper;

/**
 * 车辆Service
 * 
 * @author lgf
 * @version 2017-10-16
 */
@Service
@Transactional(readOnly = true)
public class CarService extends CrudService<CarMapper, Car> {

	public Car get(String id) {
		return super.get(id);
	}

	public List<Car> findList(Car car) {
		return super.findList(car);
	}

	public Page<Car> findPage(Page<Car> page, Car car) {
		return super.findPage(page, car);
	}

	@Transactional(readOnly = false)
	public void save(Car car) {
		super.save(car);
	}

	@Transactional(readOnly = false)
	public void delete(Car car) {
		super.delete(car);
	}

}
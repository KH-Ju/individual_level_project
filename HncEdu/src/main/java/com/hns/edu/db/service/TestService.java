package com.hns.edu.db.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hns.edu.db.mapper.TestMapper;
import com.hns.edu.db.dto.Test;
@Service
public class TestService {
	
	@Autowired
	TestMapper testMapper;
	
	public List<Test> getAll() throws Exception{
		return testMapper.getAll();
	}
	
	public List<Map<String, Object>> getAllMap() throws Exception{
		return testMapper.getAllMap();
	}

}

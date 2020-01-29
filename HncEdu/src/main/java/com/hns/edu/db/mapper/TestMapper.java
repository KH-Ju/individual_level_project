package com.hns.edu.db.mapper;

import java.util.List;
import java.util.Map;

import com.hns.edu.db.dto.Test;;

public interface TestMapper {
	
	public List<Test> getAll() throws Exception;
	
	public List<Map<String, Object>> getAllMap() throws Exception;
	
}

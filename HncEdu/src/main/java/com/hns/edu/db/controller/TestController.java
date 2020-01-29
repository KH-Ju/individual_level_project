package com.hns.edu.db.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hns.edu.db.dto.Test;
import com.hns.edu.db.service.TestService;

@Controller
public class TestController {

	@Autowired
	TestService testService;
	
	@RequestMapping("/query")
	public @ResponseBody List<Test> query() throws Exception{
		return testService.getAll();
	}
	
	@RequestMapping("/query1")
	public @ResponseBody List<Map<String, Object>> query1() throws Exception{
		return testService.getAllMap();
	}
	
}

package com.hns.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // @ResponseBody 포함안됨.
public class CommonController2 {
	
	@RequestMapping("/2")
	public String root_test() throws Exception {
		return "Hello Root(/)";
	}
	
	@RequestMapping("/demo2")
	public @ResponseBody String domo_test() throws Exception {
		return "Hello demo(/demo)";
	}
}

package com.hns.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller // @RestController Restful Api를 사용하는 컨트롤러 , @ResponseBody 포함
public class CommonController {

	@RequestMapping("/")
	public String root_test() throws Exception {
		return "main";
	}
	
	@RequestMapping("/demo") //error  @ResponseBody 를 써 주어야 함.
	public String domo_test() throws Exception {
		return "Hello demo(/demo)";
	}
}
